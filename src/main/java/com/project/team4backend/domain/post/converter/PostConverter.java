package com.project.team4backend.domain.post.converter;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import com.project.team4backend.domain.post.dto.request.PostReqDTO;
import com.project.team4backend.domain.post.entity.Post;
import com.project.team4backend.domain.post.entity.PostImage;
import org.springframework.data.domain.Page;

import java.util.List;

public class PostConverter {
    public static Post toEntity(PostReqDTO.PostCreateReqDTO req, Member member) {
        Post post = Post.builder()
                .title(req.title())
                .content(req.content())
                .tags(req.tags())
                .member(member)
                .build();

        if (req.images() != null) {
            req.images().forEach(dto ->
                    post.getImages().add(PostImage.builder()
                            .post(post)
                            .imageUrl(dto.imageUrl())
                            .imageUrlKey(dto.imageUrlKey())
                            .build()
                    )
            );
        }

        return post;
    }

    public static PostResDTO.PostDetailResDTO toDetailDTO(Post post, Member member,
                                                          boolean liked, boolean scrapped,
                                                          int likeCount, int scrapCount, int commentCount) {
        return PostResDTO.PostDetailResDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .tags(post.getTags())
                .images(post.getImages().stream()
                        .map(img -> new PostResDTO.PostDetailResDTO.ImageDTO(
                                img.getImageUrl(), img.getImageUrlKey()))
                        .toList())
                .authorNickname(post.getMember().getNickname())
                .liked(liked)
                .scrapped(scrapped)
                .likeCount(likeCount)
                .scrapCount(scrapCount)
                .commentCount(commentCount)
                .createdAt(post.getCreatedAt())
                .build();
    }
    public static PostResDTO.PostUpdateResDTO toUpdateDTO(Post post) {
        return PostResDTO.PostUpdateResDTO.builder()
                .postId(post.getPostId())
                .message("게시글이 수정되었습니다.")
                .build();
    }

    public static PostResDTO.PostDeleteResDTO toDeleteDTO(Long postId) {
        return PostResDTO.PostDeleteResDTO.builder()
                .postId(postId)
                .message("게시글이 삭제되었습니다.")
                .build();
    }

    public static PostResDTO.PostSimpleDTO toPostSimpleDTO(Post post) {
        String preview = post.getContent().length() > 100
                ? post.getContent().substring(0, 100) + "..."
                : post.getContent();

        return PostResDTO.PostSimpleDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(preview) // ✅ 여기!
                .authorNickname(post.getMember().getNickname())
                .tags(post.getTags())
                .thumbnailImageUrl(
                        post.getImages().isEmpty() ? null : post.getImages().get(0).getImageUrl()
                )
                .createdAt(post.getCreatedAt())
                .build();
    }

    public static PostResDTO.PostPageResDTO toPostPageDTO(Page<Post> postPage, List<PostResDTO.PostSimpleDTO> posts) {
        return PostResDTO.PostPageResDTO.builder()
                .posts(posts)
                .currentPage(postPage.getNumber() + 1) // 보통 1부터 시작하게 가공
                .totalPages(postPage.getTotalPages())
                .totalElements(postPage.getTotalElements())
                .isLast(postPage.isLast())
                .build();
    }

}
