package com.project.team4backend.domain.meal.repository;

import com.project.team4backend.domain.meal.entity.MealCheck;
import com.project.team4backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MealCheckRepository extends JpaRepository<MealCheck, Long> {
    Optional<MealCheck> findByMemberAndDate(Member member, LocalDate date);
}
