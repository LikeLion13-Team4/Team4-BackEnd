package com.project.team4backend.domain.post.dto.reponse;

import com.project.team4backend.domain.post.enums.PostTagType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class PostResDTO {
    @Builder
    public record PostCreateResDTO(
            Long postId,
            String message
    ){}

    @Builder
    public record PostDetailResDTO(
            Long postId,
            String title,
            String content,
            Set<PostTagType> tags,
            List<ImageDTO> images,
            String authorNickname,
            boolean liked,
            boolean scrapped,
            int likeCount,
            int scrapCount,
            int commentCount,
            LocalDateTime createdAt
    ) {
        public record ImageDTO(
                String imageUrl,
                String imageUrlKey
        ) {}
    }

    @Builder
    public record PostUpdateResDTO(
            Long postId,
            String message
    ) {}

    @Builder
    public record PostDeleteResDTO(
            Long postId,
            String message
    ) {}

    @Builder
    public record PostSimpleDTO(
            Long postId,
            String title,
            String content,
            String authorNickname,
            Set<PostTagType> tags,
            String thumbnailImageUrl,
            LocalDateTime createdAt
    ) {}

    @Builder
    public record PostPageResDTO(
            List<PostSimpleDTO> posts,
            int currentPage,
            int totalPages,
            long totalElements,
            boolean isLast
    ) {}


}
