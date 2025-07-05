package com.project.team4backend.domain.report.repository;

import com.project.team4backend.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.LocalDate;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Modifying
    void deleteByMemberIdAndStartDateAndEndDate(Long memberId, LocalDate start, LocalDate end);
}
