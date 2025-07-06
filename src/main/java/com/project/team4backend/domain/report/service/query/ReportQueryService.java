package com.project.team4backend.domain.report.service.query;


import com.project.team4backend.domain.report.dto.response.ReportResDTO;

public interface ReportQueryService {
    ReportResDTO.ReportRes getReport(Long memberId);
}
