package com.project.team4backend.domain.report.entity;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "report")
public class Report extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "total_calories")
    private Double totalCalories;

    @Column(name = "average_calories")
    private Double averageCalories;

    @Column(name = "carbs_ratio") // ex. 45.6 (%)
    private Double carbsRatio;

    @Column(name = "protein_ratio")
    private Double proteinRatio;

    @Column(name = "fat_ratio")
    private Double fatRatio;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "feedback_text", columnDefinition = "TEXT")
    private String feedbackText;

    @Column(name = "feedback_generated_at")
    private LocalDateTime feedbackGeneratedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public void updateFeedback(String feedback) {
        this.feedbackText = feedback;
        this.feedbackGeneratedAt = LocalDateTime.now();
    }
}
