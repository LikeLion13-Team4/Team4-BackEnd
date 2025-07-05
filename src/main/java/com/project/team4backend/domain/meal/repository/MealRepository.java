package com.project.team4backend.domain.meal.repository;

import com.project.team4backend.domain.meal.entity.Meal;
import com.project.team4backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {
    //해당 날짜에 회원이 등록한 식단 기록 조회
    List<Meal> findAllByMemberAndDateOrderByMealIdAsc(Member member, LocalDate date);

    //특정 멤버가 startDate부터 endDate까지 동안 기록한 모든 식단을 조회
    List<Meal> findAllByMemberAndDateBetween(Member member, LocalDate startDate, LocalDate endDate);
}
