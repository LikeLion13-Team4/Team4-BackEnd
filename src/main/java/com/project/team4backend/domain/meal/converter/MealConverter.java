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
                .menu(dto.menu())
                .description(dto.description())
                .date(dto.date())
                .calories(dto.calories())
                .carbs(dto.carbs())
                .protein(dto.protein())
                .fat(dto.fat())
                .member(member)
                .build();
    }

    public static void updateEntity(Meal meal, MealReqDTO.MealReq dto) {
        meal.updateMeal(
                dto.menu(),
                dto.description(),
                dto.calories(),
                dto.carbs(),
                dto.protein(),
                dto.fat()
        );
    }

    public static MealResDTO.MealRes toDTO(Meal meal) {
        return new MealResDTO.MealRes(
                meal.getMealId(),
                meal.getDate(),
                meal.getDescription(),
                meal.getMenu(),
                meal.getCalories(),
                meal.getCarbs(),
                meal.getProtein(),
                meal.getFat()
        );
    }


}
