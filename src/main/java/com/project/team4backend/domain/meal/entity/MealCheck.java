package com.project.team4backend.domain.meal.entity;

import com.project.team4backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "meal_checks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"date", "member_id"})
})// 회원id와 날자로 복합키 설정
public class MealCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "is_checked")
    @Builder.Default
    private Boolean isChecked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}

