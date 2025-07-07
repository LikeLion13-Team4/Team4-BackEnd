package com.project.team4backend.domain.post.dto.reponse;

import lombok.Builder;

public class PostResDTO {
    @Builder
    public record PostCreateResDTO(
            Long postId,
            String message
    ){}
}
