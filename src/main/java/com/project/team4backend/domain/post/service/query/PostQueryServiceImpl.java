package com.project.team4backend.domain.post.service.query;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.exception.MemberErrorCode;
import com.project.team4backend.domain.member.exception.MemberException;
import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.domain.post.converter.PostConverter;
import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import com.project.team4backend.domain.post.entity.Post;
import com.project.team4backend.domain.post.exception.PostErrorCode;
import com.project.team4backend.domain.post.exception.PostException;
import com.project.team4backend.domain.post.repository.PostLikeRepository;
import com.project.team4backend.domain.post.repository.PostRepository;
import com.project.team4backend.domain.post.repository.PostScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostScrapRepository postScrapRepository;

    @Override
    public PostResDTO.PostDetailResDTO getPostDetail(Long postId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));

        boolean liked = postLikeRepository.existsByPostAndMember(post, member);
        boolean scrapped = postScrapRepository.existsByPostAndMember(post, member);

        int likeCount = postLikeRepository.countByPost(post);
        int scrapCount = postScrapRepository.countByPost(post);
        int commentCount = post.getComments().size(); // 연관관계에 comments 있음

        return PostConverter.toDetailDTO(post, member, liked, scrapped, likeCount, scrapCount, commentCount);

    }
}
