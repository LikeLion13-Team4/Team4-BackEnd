package com.project.team4backend.domain.meal.service.query;

import com.project.team4backend.domain.meal.converter.MealConverter;
import com.project.team4backend.domain.meal.dto.response.MealResDTO;
import com.project.team4backend.domain.meal.entity.Meal;
import com.project.team4backend.domain.meal.repository.MealRepository;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.exception.MemberErrorCode;
import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.global.apiPayload.exception.CustomException;
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
    private final MemberRepository memberRepository;

    @Override
    public List<MealResDTO.MealRes> getMealsByDate(LocalDate date, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        List<Meal> meals = mealRepository.findAllByMemberAndDateOrderByMealIdAsc(member, date);
        return meals.stream()
                .map(MealConverter::toDTO)
                .toList();
    }

}
