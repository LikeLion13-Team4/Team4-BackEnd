package com.project.team4backend.domain.report.controller;

import com.project.team4backend.domain.report.dto.request.ReportReqDTO;
import com.project.team4backend.domain.report.dto.response.ReportResDTO;
import com.project.team4backend.domain.report.service.command.ReportCommandService;
import com.project.team4backend.domain.report.service.query.ReportQueryService;
import com.project.team4backend.global.apiPayload.CustomResponse;
import com.project.team4backend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/report")
@Tag(name = "보고서 API", description = "보고서 생성 및 gpt 피드백 관련 api입니다.")
public class ReportController {

    private final ReportCommandService reportCommandService;
    private final ReportQueryService reportQueryService;

    @PostMapping
    public CustomResponse<ReportResDTO.ReportRes> createReport(
            @RequestBody ReportReqDTO.ReportCreateDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getId();
        return CustomResponse.onSuccess(
                reportCommandService.createReport(request.startDate(), request.endDate(), memberId)
        );
    }

    @PostMapping("/feedback")
    public CustomResponse<String> generateFeedback(
            @RequestBody ReportReqDTO.ReportCreateDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long memberId = userDetails.getId(); //
        reportCommandService.generateFeedback(request.startDate(), request.endDate(), memberId);
        return CustomResponse.onSuccess("AI 피드백이 생성되었습니다.");
    }
}