package com.project.team4backend.domain.report.service.command;

import com.project.team4backend.domain.meal.entity.Meal;
import com.project.team4backend.domain.meal.exception.MealErrorCode;
import com.project.team4backend.domain.meal.repository.MealRepository;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.exception.MemberErrorCode;
import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.domain.report.converter.ReportConverter;
import com.project.team4backend.domain.report.dto.response.ReportResDTO;
import com.project.team4backend.domain.report.entity.Report;
import com.project.team4backend.domain.report.excption.ReportErrorCode;
import com.project.team4backend.domain.report.repository.ReportRepository;
import com.project.team4backend.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportCommandServiceImpl implements ReportCommandService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final MealRepository mealRepository;

    @Override
    public ReportResDTO.ReportRes createReport(LocalDate start, LocalDate end, Long memberId) {
        // 1. Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 2. 기존 보고서가 있으면 삭제 또는 갱신 처리
        reportRepository.deleteByMemberIdAndStartDateAndEndDate(memberId, start, end); // 선택적

        // 3. 기간 내 Meal 목록 조회
        List<Meal> meals = mealRepository.findAllByMemberAndDateBetween(member, start, end);
        if (meals.isEmpty()) {
            throw new CustomException(MealErrorCode.MEAL_NOT_FOUND);
        }

        // 4. 총 영양소 계산
        double totalCalories = meals.stream().mapToDouble(Meal::getCalories).sum();
        double totalCarbs = meals.stream().mapToDouble(Meal::getCarbs).sum();
        double totalProtein = meals.stream().mapToDouble(Meal::getProtein).sum();
        double totalFat = meals.stream().mapToDouble(Meal::getFat).sum();

        long days = ChronoUnit.DAYS.between(start, end) + 1;
        double averageCalories = days > 0 ? totalCalories / days : 0;

        double totalMacros = totalCarbs + totalProtein + totalFat;
        double carbsRatio = totalMacros > 0 ? (totalCarbs / totalMacros) * 100 : 0;
        double proteinRatio = totalMacros > 0 ? (totalProtein / totalMacros) * 100 : 0;
        double fatRatio = totalMacros > 0 ? (totalFat / totalMacros) * 100 : 0;

        // 5. Report 생성 및 저장
        Report report = ReportConverter.toReportEntity(
                start, end, member,
                totalCalories, averageCalories,
                carbsRatio, proteinRatio, fatRatio
        );

        reportRepository.save(report);

        // 6. DTO 변환 후 반환
        return ReportConverter.toReportResDTO(report);
    }

    @Override
    public void generateFeedback(LocalDate start, LocalDate end, Long memberId) {
        /*
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 1. 기간 내 식사 조회
        List<Meal> meals = mealRepository.findAllByMemberAndDateBetween(member, start, end);

        if (meals.isEmpty()) {
            throw new CustomException(MealErrorCode.MEAL_NOT_FOUND);
        }

        // 2. GPT 프롬프트 생성 (예: 음식 이름, 시간, 영양소 등 기반)
        String prompt = gptPromptBuilder.buildFromMeals(meals);

        // 3. GPT 호출 (예시)
        String feedback = gptClient.generateFeedback(prompt);

        // 4. 보고서 조회 or 생성
        Report report = reportRepository.findByMemberIdAndStartDateAndEndDate(memberId, start, end)
                .orElseThrow(() -> new CustomException(ReportErrorCode.R));

        // 5. 피드백 저장
        report.updateFeedback(feedback);
        */
    }
}