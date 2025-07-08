package com.project.team4backend.domain.image.dto.request;

import lombok.Builder;

public class ImageReqDTO {
    @Builder
    public record PresignedUrlReqDTO(
            String fileExtension,
            String contentType
    ) {
    }
    @Builder
    public record SaveImageReqDTO(
            String fileKey
    ){
    }
}
