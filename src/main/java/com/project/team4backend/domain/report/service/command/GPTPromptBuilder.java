package com.project.team4backend.domain.report.service.command;

import com.project.team4backend.domain.meal.entity.Meal;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GPTPromptBuilder {

    public String buildFromMeals(List<Meal> meals) {
        return meals.stream()
                .sorted(Comparator.comparing(Meal::getDate))
                .map(meal -> String.format(
                        "날짜: %s, 메뉴: %s, 칼로리: %.0f kcal, 탄수화물: %.1fg, 단백질: %.1fg, 지방: %.1fg",
                        meal.getDate(), meal.getMenu(), meal.getCalories(),
                        meal.getCarbs(), meal.getProtein(), meal.getFat()
                ))
                .collect(Collectors.joining("\n"))
                + "\n\n위 식단을 기반으로 건강한 식습관 피드백을 작성해주세요.";
    }
}
