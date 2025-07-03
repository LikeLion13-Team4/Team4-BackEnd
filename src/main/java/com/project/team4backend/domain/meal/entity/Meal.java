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
public class Meal {

    @Id // 기본 키(Primary Key) 설정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 전략 (데이터베이스가 ID를 생성)
    @Column(name = "meal_id")
    private Long mealId;

    @Column(name = "menu")
    private String menu; // 메뉴

    @Column(name = "date", nullable = false) // 식단 날짜.
    private LocalDate date;

    @Column(name = "description", columnDefinition = "TEXT") // 식단 설명. 긴 텍스트를 위해 TEXT 타입 지정 가능
    private String description;

    @Column(name = "calories") // 칼로리
    private Double calories;

    @Column(name = "carbs") // 탄수화물
    private Double carbs;

    @Column(name = "protein") // 단백질
    private Double protein;

    @Column(name = "fat") // 지방
    private Double fat;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 어떤 회원이 등록한 식단인지

    public void updateMeal(String menu,String description, Double calories, Double carbs, Double protein, Double fat) {
        this.menu = menu;
        this.description = description;
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.fat = fat;
    }
}
