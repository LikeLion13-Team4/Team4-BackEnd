package com.project.team4backend.domain.image.dto.internel;

import lombok.Builder;

import java.time.LocalDateTime;

public class ImageInternelDTO {
    @Builder
    public record ImageTrackingResDTO (
            String fileKey,
            LocalDateTime createAt
    ){}
}
