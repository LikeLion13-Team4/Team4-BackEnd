package com.project.team4backend.domain.post.service.command;

import com.project.team4backend.domain.image.exception.ImageErrorCode;
import com.project.team4backend.domain.image.exception.ImageException;
import com.project.team4backend.domain.image.service.RedisImageTracker;
import com.project.team4backend.domain.image.service.command.ImageCommandService;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.exception.MemberErrorCode;
import com.project.team4backend.domain.member.exception.MemberException;
import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.domain.post.converter.PostConverter;
import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import com.project.team4backend.domain.post.dto.request.PostReqDTO;
import com.project.team4backend.domain.post.entity.Post;
import com.project.team4backend.domain.post.entity.PostLike;
import com.project.team4backend.domain.post.entity.PostScrap;
import com.project.team4backend.domain.post.entity.PostImage;
import com.project.team4backend.domain.post.exception.PostErrorCode;
import com.project.team4backend.domain.post.exception.PostException;
import com.project.team4backend.domain.post.repository.PostLikeRepository;
import com.project.team4backend.domain.post.repository.PostRepository;
import com.project.team4backend.domain.post.repository.PostScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    private final PostLikeRepository postLikeRepository;
    private final PostScrapRepository postScrapRepository;
    private final ImageCommandService imageCommandService;
    private final RedisImageTracker redisImageTracker;

    @Override
    public PostResDTO.PostCreateResDTO createPost(PostReqDTO.PostCreateReqDTO dto, String email) {
        // 멤버 검증
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new PostException(PostErrorCode.MEMBER_NOT_FOUND));
        // 이미지 선택 했다면
        if (dto.images() != null && dto.images().size() > 5) {
            throw new ImageException(ImageErrorCode.IMAGE_TOO_MANY_REQUESTS);
        }

        // presigned 검증 및 commit 처리
        if (dto.images() != null) {
            for (PostReqDTO.PostCreateReqDTO.ImageDTO image : dto.images()) {
                if (!redisImageTracker.isOwnedByUser(email, image.imageUrlKey())) {
                    throw new ImageException(ImageErrorCode.IMAGE_INVALID_FILE_KEY);
                }
                redisImageTracker.remove(email, image.imageUrlKey());
            }
        }

        Post post = PostConverter.toEntity(dto, member);

        Post saved = postRepository.save(post);

        return PostConverter.toCreateDTO(saved);
    }

    @Override
    public PostResDTO.PostUpdateResDTO updatePost(Long postId, PostReqDTO.PostUpdateReqDTO postUpdateReqDTO, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getMember().equals(member)) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_POST_UPDATE);
        }

        // 기존 이미지 중 삭제 대상만 삭제
        List<String> newFileKeys = postUpdateReqDTO.images().stream()
                .map(PostReqDTO.PostUpdateReqDTO.ImageDTO::imageUrlKey)
                .toList();

        List<PostImage> toDelete = post.getImages().stream()
                .filter(existing -> !newFileKeys.contains(existing.getImageUrlKey()))
                .toList();

        toDelete.forEach(img -> imageCommandService.delete(email, img.getImageUrlKey()));

        // 커밋 처리: 새로 추가된 이미지 중 기존에 없던 fileKey만 커밋 때문에 Set 사용 O(1)
        Set<String> existingFileKeys = post.getImages().stream()
                .map(PostImage::getImageUrlKey)
                .collect(Collectors.toSet());

        List<String> newAddedFileKeys = newFileKeys.stream()
                .filter(fileKey -> !existingFileKeys.contains(fileKey)) // 기존에 없던 fileKey만
                .toList();

        newAddedFileKeys.forEach(fileKey -> imageCommandService.commit(email, fileKey));

        // update는 Post 엔티티 내부 메서드로 실행
        List<PostImage> images = PostConverter.toImageEntities(postUpdateReqDTO.images(), post);
        post.update(postUpdateReqDTO.title(), postUpdateReqDTO.content(), postUpdateReqDTO.tags(), images);
        return PostConverter.toUpdateDTO(post);
    }

    @Override
    public PostResDTO.PostDeleteResDTO deletePost(Long postId, String email) {
        // 1. 사용자 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 2. 게시글 조회 (작성자와 이미지 fetch)
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        // 3. 작성자 확인
        if (!post.getMember().equals(member)) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_POST_DELETE);
        }
        // s3 + redis  모든 이미지 삭제
        post.getImages().forEach(image -> imageCommandService.delete(email, image.getImageUrlKey()));

        // 4. 삭제
        postRepository.delete(post);

        return PostConverter.toDeleteDTO(postId);
    }

    // 좋아요 증가 감소
    @Override
    public PostResDTO.ToggleResDTO toggleLike(Long postId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new PostException(PostErrorCode.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        Optional<PostLike> existing = postLikeRepository.findByPostAndMember(post, member); //해당 유저가 이미 눌렀는지 확인

        boolean toggled;
        if (existing.isPresent()) {
            postLikeRepository.delete(existing.get()); // 이미눌렀면 삭제
            toggled = false;
        } else {
            PostLike like = PostLike.builder().member(member).post(post).build(); //안눌렀으면 멤버,포스트id를
            postLikeRepository.save(like); // like엔티티에 저장
            toggled = true;
        }

        int likeCount = postLikeRepository.countByPost(post);
        return PostConverter.toToggleDTO(toggled, likeCount); //증가(true)인지 감소(false)인지와 수정 후 개수 전달
    }

    @Override
    public PostResDTO.ToggleResDTO toggleScrap(Long postId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new PostException(PostErrorCode.MEMBER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        Optional<PostScrap> existing = postScrapRepository.findByPostAndMember(post, member);

        boolean toggled;
        if (existing.isPresent()) {
            postScrapRepository.delete(existing.get());
            toggled = false;
        } else {
            PostScrap scrap = PostScrap.builder().member(member).post(post).build();
            postScrapRepository.save(scrap);
            toggled = true;
        }

        int scrapCount = postScrapRepository.countByPost(post);
        return PostConverter.toToggleDTO(toggled, scrapCount);
    }



}
