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
import com.project.team4backend.domain.post.entity.PostImage;
import com.project.team4backend.domain.post.exception.PostErrorCode;
import com.project.team4backend.domain.post.exception.PostException;
import com.project.team4backend.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
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
    public PostResDTO.PostUpdateResDTO updatePost(Long postId, PostReqDTO.PostUpdateReqDTO dto, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getMember().equals(member)) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_POST_UPDATE);
        }

        // update는 Post 엔티티 내부 메서드로 실행
        List<PostImage> images = PostConverter.toImageEntities(dto.images(), post);
        post.update(dto.title(), dto.content(), dto.tags(), images);

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

        // 4. 삭제
        postRepository.delete(post);

        return PostConverter.toDeleteDTO(postId);
    }


}
