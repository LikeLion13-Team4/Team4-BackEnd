package com.project.team4backend.domain.report.service.command;

import com.project.team4backend.domain.report.dto.response.ReportResDTO;

import java.time.LocalDate;

public interface ReportCommandService {
    ReportResDTO.ReportRes createReport(LocalDate startDate, LocalDate endDate, Long memberId);
    void generateFeedback(LocalDate startDate, LocalDate endDate, Long memberId);
}
