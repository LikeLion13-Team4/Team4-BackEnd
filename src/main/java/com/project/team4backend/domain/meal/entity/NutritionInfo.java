package com.project.team4backend.domain.meal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NutritionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nutritionId;

    @Column(name = "calories")
    private Double calories;

    @Column(name = "carbs")
    private Double carbs;

    @Column(name = "protein")
    private Double protein;

    @Column(name = "fat")
    private Double fat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;
}
