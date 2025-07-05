package com.project.team4backend.domain.report.service.query;

import com.project.team4backend.domain.report.dto.response.ReportResDTO;
import com.project.team4backend.domain.report.entity.Report;
import com.project.team4backend.domain.report.repository.ReportRepository;
import com.project.team4backend.global.apiPayload.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportQueryServiceImpl implements ReportQueryService {
    private final ReportRepository reportRepository;

    public ReportResDTO getReportByRange(LocalDate start, LocalDate end, Long memberId) {
        Report report = reportRepository.findByMemberAndStartDateAndEndDate(member, start, end)
                .orElseThrow(() -> new CustomException(ErrorCode.REPORT_NOT_FOUND));
        return ReportResDTO.from(report);
    }
}
