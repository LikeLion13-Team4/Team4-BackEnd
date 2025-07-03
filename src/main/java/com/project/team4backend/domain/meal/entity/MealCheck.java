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
public class MealCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long checkId;

    @Column(name = "date", unique = true)
    private LocalDate date;

    @Column(name = "is_checked")
    private Boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}

