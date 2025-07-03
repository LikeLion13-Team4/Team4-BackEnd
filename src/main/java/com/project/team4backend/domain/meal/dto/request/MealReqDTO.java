package com.project.team4backend.domain.meal.dto.request;

import lombok.Builder;

import java.time.LocalDate;

public class MealReqDTO {
    @Builder
    public record MealReq(
            LocalDate date,
            String menu,
            String description,
            Double calories,
            Double carbs,
            Double protein,
            Double fat
    ) {}
}
