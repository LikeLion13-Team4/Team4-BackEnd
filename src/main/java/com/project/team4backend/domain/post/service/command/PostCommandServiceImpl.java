package com.project.team4backend.domain.post.service.command;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.domain.post.converter.PostConverter;
import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import com.project.team4backend.domain.post.dto.request.PostReqDTO;
import com.project.team4backend.domain.post.entity.Post;
import com.project.team4backend.domain.post.exception.PostErrorCode;
import com.project.team4backend.domain.post.exception.PostException;
import com.project.team4backend.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

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
    public void updatePost(Long postId, PostReqDTO.PostUpdateReqDTO dto, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new PostException(PostErrorCode.MEMBER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        if (!post.getMember().equals(member)) {
            throw new PostException(PostErrorCode.UNAUTHORIZED_POST_UPDATE);
        }

        // update는 Post 엔티티 내부 메서드로 실행
        post.update(dto.title(), dto.content(), dto.tags(), dto.images());
    }

    @Override
    public void deletePost(Long postId, String email) {
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
    }


}
