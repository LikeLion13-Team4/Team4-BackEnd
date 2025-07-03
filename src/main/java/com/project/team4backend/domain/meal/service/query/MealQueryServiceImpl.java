package com.project.team4backend.domain.meal.service.query;

import com.project.team4backend.domain.meal.converter.MealConverter;
import com.project.team4backend.domain.meal.dto.response.MealResDTO;
import com.project.team4backend.domain.meal.entity.Meal;
import com.project.team4backend.domain.meal.repository.MealRepository;
import com.project.team4backend.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MealQueryServiceImpl implements MealQueryService {

    private final MealRepository mealRepository;

    @Override
    public List<MealResDTO.MealRes> getMealsByDate(LocalDate date, Member member) {
        List<Meal> meals = mealRepository.findAllByMemberAndDateOrderByMealIdAsc(member, date);
        return meals.stream()
                .map(MealConverter::toDTO)
                .toList();
    }
}
