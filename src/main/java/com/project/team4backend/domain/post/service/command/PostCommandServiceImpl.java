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
}
