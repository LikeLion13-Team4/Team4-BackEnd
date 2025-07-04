package com.project.team4backend.domain.meal.service.command;

import com.project.team4backend.domain.meal.converter.MealConverter;
import com.project.team4backend.domain.meal.dto.request.MealReqDTO;
import com.project.team4backend.domain.meal.entity.Meal;
import com.project.team4backend.domain.meal.entity.MealCheck;
import com.project.team4backend.domain.meal.exception.MealErrorCode;
import com.project.team4backend.domain.meal.repository.MealCheckRepository;
import com.project.team4backend.domain.meal.repository.MealRepository;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.exception.MemberErrorCode;
import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class MealCommandServiceImpl implements MealCommandService {

    private final MealRepository mealRepository;
    private final MealCheckRepository mealCheckRepository;
    private final MemberRepository memberRepository;

    @Override
    public void createMeal(MealReqDTO.MealReq dto, String email) {
        // email → Member 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 식단 저장
        Meal meal = MealConverter.toEntity(dto, member);
        mealRepository.save(meal);

        // 자동 출석 체크
        LocalDate today = LocalDate.now();

        boolean alreadyChecked = mealCheckRepository.findByMemberAndDate(member, today).isPresent();
        if (!alreadyChecked) {
            MealCheck check = MealCheck.builder()
                    .date(today)
                    .isChecked(true)
                    .member(member)
                    .build();
            mealCheckRepository.save(check);
        }
    }

    @Override
    public void updateMeal(Long mealId, MealReqDTO.MealReq req, String email) {
        Member member = getMemberByEmail(email);
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new CustomException(MealErrorCode.MEAL_NOT_FOUND));

        if (!meal.getMember().equals(member)) {
            throw new CustomException(MealErrorCode.UNAUTHORIZED_MEAL_ACCESS);
        }

        MealConverter.updateEntity(meal, req);
    }


    @Override
    public void deleteMeal(Long mealId, String email) {
        Member member = getMemberByEmail(email);
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new CustomException(MealErrorCode.MEAL_NOT_FOUND));

        if (!meal.getMember().equals(member)) {
            throw new CustomException(MealErrorCode.UNAUTHORIZED_MEAL_ACCESS);
        }

        mealRepository.delete(meal);
    }



    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));
    }


}
