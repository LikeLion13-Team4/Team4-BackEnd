package com.project.team4backend.domain.meal.repository;

import com.project.team4backend.domain.meal.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long> {
}
