package com.project.team4backend.domain.report.repository;

import com.project.team4backend.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    @Modifying
    @Query("DELETE FROM Report r WHERE r.member.id = :memberId")
    void deleteByMemberId(@Param("memberId") Long memberId);

    Optional<Report> findByMemberIdAndStartDateAndEndDate(Long memberId, LocalDate startDate, LocalDate endDate);

    Optional<Report> findByMemberId(Long memberId);
}
