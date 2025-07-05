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
                + "\n\n위 식단을 기반으로 건강한 식습관 피드백을 작성해주세요. 날짜는 신경쓰지말고 전체적인 맥락에서의 조언이 필요합니다" +
                "사용자 설정이기 때문에 일부는 값이 누락될 수도 있습니다 값이 누락되어있으면 그것을 고려해서 조언" +
                "400토큰 제한이니 답변 길이 맞추기";
    }
}
