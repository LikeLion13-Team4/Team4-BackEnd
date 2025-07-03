package com.project.team4backend.domain.meal.repository;

import com.project.team4backend.domain.meal.entity.MealCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealCheckRepository extends JpaRepository<MealCheck, Long> {
}
