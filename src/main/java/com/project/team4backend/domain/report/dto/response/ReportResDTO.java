package com.project.team4backend.domain.report.dto.response;

import lombok.Builder;

public class ReportResDTO {

    @Builder
    public record ReportRes(
            Double totalCalories,
            Double averageCalories,
            Double carbsRatio,
            Double proteinRatio,
            Double fatRatio,
            String feedbackText
    ){}
}
