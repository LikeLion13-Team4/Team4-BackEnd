package com.project.team4backend.domain.post.dto.request;

import com.project.team4backend.domain.post.entity.enums.PostTagType;
import lombok.Builder;

import java.util.List;
import java.util.Set;

public class PostReqDTO {
    @Builder
    public record PostCreateReqDTO(
            String title,
            String content,
            Set<PostTagType> tags,
            List<ImageDTO> images // 여러 이미지
    ) {
        public record ImageDTO(
                String imageUrl,
                String imageUrlKey
        ) {}
    }

    @Builder
    public record PostUpdateReqDTO(
            String title,
            String content,
            Set<PostTagType> tags,
            List<ImageDTO> images
    ) {
        public record ImageDTO(
                String imageUrl,
                String imageUrlKey
        ) {}
    }
}
