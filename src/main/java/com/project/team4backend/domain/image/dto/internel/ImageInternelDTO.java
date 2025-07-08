package com.project.team4backend.domain.image.dto.internel;

import lombok.Builder;

import java.time.LocalDate;

public class ImageInternelDTO {
    @Builder
    public record ImageTrackingResDTO (
            String fileKey,
            LocalDate createAt
    ){}
}
