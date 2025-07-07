package com.project.team4backend.domain.post.dto.request;

import com.project.team4backend.domain.post.enums.PostTagType;
import lombok.Builder;

import java.util.List;
import java.util.Set;

public class PostReqDTO {
    @Builder
    public record PostCreateReqDTO(
            String title,
            String content,
            Set<PostTagType> tags,
            List<String> imageUrls // 여러 이미지
    ) {}

    public record PostUpdateReqDTO(
            String title,
            String content,
            Set<PostTagType> tags,
            List<String> imageUrls
    ) {}
}
