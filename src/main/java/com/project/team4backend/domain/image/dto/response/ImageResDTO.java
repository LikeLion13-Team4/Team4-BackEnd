package com.project.team4backend.domain.image.dto.response;


import lombok.Builder;


public class ImageResDTO {
    @Builder
    public record PresignedUrlResDTO (
            String presignedUrl,
            String fileKey
    ){
    }

    @Builder
    public record SaveImageResDTO (
            String profileImageUrl,
            String message
    ){
    }
}
