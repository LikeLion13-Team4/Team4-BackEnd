package com.project.team4backend.domain.post.service.command;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.domain.post.converter.PostConverter;
import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import com.project.team4backend.domain.post.dto.request.PostReqDTO;
import com.project.team4backend.domain.post.entity.Post;
import com.project.team4backend.domain.post.entity.PostLike;
import com.project.team4backend.domain.post.entity.PostScrap;
import com.project.team4backend.domain.post.exception.PostErrorCode;
import com.project.team4backend.domain.post.exception.PostException;
import com.project.team4backend.domain.post.repository.PostLikeRepository;
import com.project.team4backend.domain.post.repository.PostRepository;
import com.project.team4backend.domain.post.repository.PostScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostScrapRepository postScrapRepository;


    @Override
    public PostResDTO.PostCreateResDTO createPost(PostReqDTO.PostCreateReqDTO dto, String Email) {
        // 멤버 검증
        Member member = memberRepository.findByEmail(Email)
                .orElseThrow(() -> new PostException(PostErrorCode.MEMBER_NOT_FOUND));


        Post post = PostConverter.toEntity(dto, member);


        Post saved = postRepository.save(post);

        return PostResDTO.PostCreateResDTO.builder()
                .postId(saved.getPostId())
                .message("게시글이 등록되었습니다.")
                .build();
    }

    @Override
    public PostResDTO.PostUpdateResDTO updatePost(Long postId, PostReqDTO.PostUpdateReqDTO dto, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new PostException(PostErrorCode.MEMBER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getMember().equals(member)) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_POST_UPDATE);
        }

        // update는 Post 엔티티 내부 메서드로 실행
        post.update(dto.title(), dto.content(), dto.tags(), dto.images());

        return PostConverter.toUpdateDTO(post);
    }

    @Override
    public PostResDTO.PostDeleteResDTO deletePost(Long postId, String email) {
        // 1. 사용자 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new PostException(PostErrorCode.MEMBER_NOT_FOUND));

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
