package com.project.team4backend.domain.meal.converter;

import com.project.team4backend.domain.meal.dto.request.MealReqDTO;
import com.project.team4backend.domain.meal.dto.response.MealResDTO;
import com.project.team4backend.domain.meal.entity.Meal;
import com.project.team4backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MealConverter {
    public static Meal toEntity(MealReqDTO.MealReq dto, Member member) {
        return Meal.builder()
                .date(dto.date())
                .description(dto.description())
                .calories(dto.calories())
                .carbs(dto.carbs())
                .protein(dto.protein())
                .fat(dto.fat())
                .member(member)
                .build();
    }

    public static MealResDTO.MealRes toDTO(Meal meal) {
        return new MealResDTO.MealRes(
                meal.getMealId(),
                meal.getDate(),
                meal.getDescription(),
                meal.getCalories(),
                meal.getCarbs(),
                meal.getProtein(),
                meal.getFat()
        );
    }
}
