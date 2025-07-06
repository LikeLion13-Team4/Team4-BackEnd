package com.project.team4backend.domain.report.converter;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.report.dto.response.ReportResDTO;
import com.project.team4backend.domain.report.entity.Report;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReportConverter {
    public static Report toReportEntity(
            LocalDate startDate,
            LocalDate endDate,
            Member member,
            double totalCalories,
            double averageCalories,
            double carbsRatio,
            double proteinRatio,
            double fatRatio
    ) {
        return Report.builder()
                .startDate(startDate)
                .endDate(endDate)
                .member(member)
                .totalCalories(totalCalories)
                .averageCalories(averageCalories)
                .carbsRatio(carbsRatio)
                .proteinRatio(proteinRatio)
                .fatRatio(fatRatio)
                .build();
    }
    public static ReportResDTO.ReportRes toReportResDTO(Report report) {
        return ReportResDTO.ReportRes.builder()
                .totalCalories(report.getTotalCalories())
                .averageCalories(report.getAverageCalories())
                .carbsRatio(report.getCarbsRatio())
                .proteinRatio(report.getProteinRatio())
                .fatRatio(report.getFatRatio())
                .feedbackText(report.getFeedbackText()) // null일 수도 있음
                .build();
    }

}
