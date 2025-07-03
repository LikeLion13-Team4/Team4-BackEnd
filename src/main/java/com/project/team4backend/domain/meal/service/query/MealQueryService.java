package com.project.team4backend.domain.meal.service.query;

import com.project.team4backend.domain.meal.dto.response.MealResDTO;

import java.time.LocalDate;
import java.util.List;

public interface MealQueryService {
    List<MealResDTO.MealRes> getMealsByDate(LocalDate date, String member);
}
