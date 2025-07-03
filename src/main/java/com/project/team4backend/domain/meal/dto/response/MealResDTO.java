package com.project.team4backend.domain.meal.dto.response;

import lombok.Builder;

import java.time.LocalDate;

public class MealResDTO {
    @Builder
    public record MealRes(
            Long mealId,
            LocalDate date,
            String description,
            Double calories,
            Double carbs,
            Double protein,
            Double fat
    ) {}
}
