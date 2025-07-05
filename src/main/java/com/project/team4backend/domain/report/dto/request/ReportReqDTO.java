package com.project.team4backend.domain.report.dto.request;

import lombok.Builder;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

public class ReportReqDTO {

    @Builder
    public record ReportCreateDTO(
      LocalDate startDate,
      LocalDate endDate
    ){}
}
