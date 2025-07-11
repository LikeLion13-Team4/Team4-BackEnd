package com.project.team4backend.domain.report.service.query;

import com.project.team4backend.domain.report.converter.ReportConverter;
import com.project.team4backend.domain.report.dto.response.ReportResDTO;
import com.project.team4backend.domain.report.entity.Report;
import com.project.team4backend.domain.report.excption.ReportEception;
import com.project.team4backend.domain.report.excption.ReportErrorCode;
import com.project.team4backend.domain.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportQueryServiceImpl implements ReportQueryService {
    private final ReportRepository reportRepository;


    @Override
    public ReportResDTO.ReportRes getReport(Long memberId) {
        Report report = reportRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ReportEception(ReportErrorCode.REPORT_NOT_FOUND));
        return ReportConverter.toReportResDTO(report);
    }
}
