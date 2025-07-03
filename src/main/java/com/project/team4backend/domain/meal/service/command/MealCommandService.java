package com.project.team4backend.domain.meal.service.command;

import com.project.team4backend.domain.meal.dto.request.MealReqDTO;
import com.project.team4backend.domain.member.entity.Member;

import java.time.LocalDate;

public interface MealCommandService {

    void createMeal(MealReqDTO.MealReq dto, String email);

    void updateMeal(Long mealId, MealReqDTO.MealReq req, String email);

    void deleteMeal(Long mealId, String email);
}
