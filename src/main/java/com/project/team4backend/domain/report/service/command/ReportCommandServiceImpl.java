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
    private final GPTClient gptClient;
    private final GPTPromptBuilder gptPromptBuilder;

    @Override
    public ReportResDTO.ReportRes createReport(LocalDate start, LocalDate end, Long memberId) {
        // 1. Member 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // 2. 기존 보고서 삭제 (회원당 1개의 보고서 유지)
        reportRepository.deleteByMemberId(memberId);reportRepository.deleteByMemberId(memberId);

        // 3. 기간 내 Meal 목록 조회
        List<Meal> meals = mealRepository.findAllByMemberAndDateBetween(member, start, end);
        if (meals.isEmpty()) {
            throw new CustomException(MealErrorCode.MEAL_NOT_FOUND);
        }

        // 4. 총 영양소 계산 (null 허용)
        double totalCalories = meals.stream()
                .filter(m -> m.getCalories() != null)
                .mapToDouble(Meal::getCalories)
                .sum();

        double totalCarbs = meals.stream()
                .filter(m -> m.getCarbs() != null)
                .mapToDouble(Meal::getCarbs)
                .sum();

        double totalProtein = meals.stream()
                .filter(m -> m.getProtein() != null)
                .mapToDouble(Meal::getProtein)
                .sum();

        double totalFat = meals.stream()
                .filter(m -> m.getFat() != null)
                .mapToDouble(Meal::getFat)
                .sum();

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



        // 1. 회원 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberErrorCode.MEMBER_NOT_FOUND));

        // isRequestAllowed 메서드를 호출하여 GPT 요청이 허용되는지 확인
        if (!isRequestAllowed(member)) {
            // 허용되지 않으면 예외 발생
            throw new CustomException(ReportErrorCode.GPT_CALL_LIMIT_EXCEEDED);
        }

        // 2. 기간 내 식사 조회
        List<Meal> meals = mealRepository.findAllByMemberAndDateBetween(member, start, end);

        if (meals.isEmpty()) {
            throw new CustomException(MealErrorCode.MEAL_NOT_FOUND);
        }

        // 3. GPT 프롬프트 생성
        String prompt = gptPromptBuilder.buildFromMeals(meals);

        // 4. GPT 호출
        String feedback = gptClient.generateFeedback(prompt);

        // 5. 보고서 조회
        Report report = reportRepository.findByMemberIdAndStartDateAndEndDate(memberId, start, end)
                .orElseThrow(() -> new CustomException(ReportErrorCode.REPORT_NOT_FOUND)); // 예외 수정 필요

        // 6. 피드백 저장
        report.updateFeedback(feedback);
    }

    //gpt 호출제한 확인 매서드
    public boolean isRequestAllowed(Member member) {
        LocalDate today = LocalDate.now();

        if (member.getLastRequestDate() == null || !member.getLastRequestDate().isEqual(today)) {
            member.setRequestCount(1);
            member.setLastRequestDate(today);
            memberRepository.save(member);
            return true;
        } //날이 지나거나 처음이면 호출횟수 1로 초기화

        if (member.getRequestCount() < 3) { //3번 횟수제한
            member.setRequestCount(member.getRequestCount() + 1);
            memberRepository.save(member);
            return true;
        }

        return false;
    }
}