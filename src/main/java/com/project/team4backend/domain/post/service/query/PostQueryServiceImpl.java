package com.project.team4backend.domain.post.service.query;

import com.project.team4backend.domain.comment.repository.CommentRepository;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.exception.MemberErrorCode;
import com.project.team4backend.domain.member.exception.MemberException;
import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.domain.post.converter.PostConverter;
import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import com.project.team4backend.domain.post.entity.Post;
import com.project.team4backend.domain.post.entity.PostLike;
import com.project.team4backend.domain.post.entity.PostScrap;
import com.project.team4backend.domain.post.exception.PostErrorCode;
import com.project.team4backend.domain.post.exception.PostException;
import com.project.team4backend.domain.post.repository.PostLikeRepository;
import com.project.team4backend.domain.post.repository.PostRepository;
import com.project.team4backend.domain.post.repository.PostScrapRepository;
import com.project.team4backend.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostQueryServiceImpl implements PostQueryService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostScrapRepository postScrapRepository;
    private final CommentRepository commentRepository;

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
        int commentCount = post.getComments().size();

        return PostConverter.toDetailDTO(post, member, liked, scrapped, likeCount, scrapCount, commentCount);

    }

    @Override
    public PostResDTO.PostPageResDTO getAllPosts(Pageable pageable) {
        Page<Post> postPage = postRepository.findAllByOrderByPostIdDesc(pageable);

        List<PostResDTO.PostSimpleDTO> posts = postPage.getContent().stream()
                .map(post -> {
                    int likeCount = postLikeRepository.countByPost(post);
                    int scrapCount = postScrapRepository.countByPost(post);
                    int commentCount = post.getComments().size();

                    return PostConverter.toPostSimpleDTO(post, likeCount, scrapCount, commentCount);
                })
                .toList();

        return PostConverter.toPostPageDTO(postPage, posts);
    }

    //스크랩 목록 조회
    @Override
    public PostResDTO.PostPageResDTO<PostResDTO.PostMyDTO> getScrappedPosts(Pageable pageable, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(PostErrorCode.MEMBER_NOT_FOUND));

        Page<PostScrap> scrapPage = postScrapRepository.findByMember(member, pageable);

        List<PostResDTO.PostMyDTO> posts = scrapPage.getContent().stream()
                .map(scrap -> PostConverter.toPostMyDTO(scrap.getPost()))
                .toList();

        return PostConverter.toPostPageDTO(scrapPage.map(PostScrap::getPost), posts);
    }

    //좋아요 목록 조회
    @Override
    public PostResDTO.PostPageResDTO<PostResDTO.PostMyDTO> getLikedPosts(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(PostErrorCode.MEMBER_NOT_FOUND));

        Page<PostLike> likedPage = postLikeRepository.findByMember(member, pageable);

        List<PostResDTO.PostMyDTO> posts = likedPage.getContent().stream()
                .map(PostLike::getPost)
                .map(PostConverter::toPostMyDTO)
                .toList();

        return PostConverter.toPostPageDTO(likedPage.map(PostLike::getPost), posts);
    }

    //댓글 단 게시글 목록 조회
    @Override
    public PostResDTO.PostPageResDTO<PostResDTO.PostMyDTO> getCommentedPosts(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(PostErrorCode.MEMBER_NOT_FOUND));

        Page<Post> postPage = commentRepository.findDistinctPostsByMember(member, pageable);

        List<PostResDTO.PostMyDTO> posts = postPage.getContent().stream()
                .map(PostConverter::toPostMyDTO)
                .toList();

        return PostConverter.toPostPageDTO(postPage, posts);
    }

    @Override
    public PostResDTO.PostPageResDTO<PostResDTO.PostMyDTO> getMyPosts(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(PostErrorCode.MEMBER_NOT_FOUND));

        Page<Post> postPage = postRepository.findAllByMember(member, pageable);

        List<PostResDTO.PostMyDTO> posts = postPage.getContent().stream()
                .map(PostConverter::toPostMyDTO)
                .toList();

        return PostConverter.toPostPageDTO(postPage, posts);
    }
}
