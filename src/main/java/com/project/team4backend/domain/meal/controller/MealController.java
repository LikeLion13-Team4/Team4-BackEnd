package com.project.team4backend.domain.meal.controller;

import com.project.team4backend.domain.meal.dto.request.MealReqDTO;
import com.project.team4backend.domain.meal.dto.response.MealResDTO;
import com.project.team4backend.domain.meal.service.command.MealCommandService;
import com.project.team4backend.domain.meal.service.query.MealQueryService;
import com.project.team4backend.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meals")
@Tag(name = "식단 API", description = "식단 등록, 수정, 삭제 및 조회 관련 API입니다.")
public class MealController {

    private final MealCommandService mealCommandService;
    private final MealQueryService mealQueryService;

    // 식단 등록
    @PostMapping
    @Operation(summary = "식단 등록", description = "회원이 새로운 식단을 등록합니다.")
    public CustomResponse<Void> createMeal(
            @RequestBody @Valid MealReqDTO.MealReq dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername(); //사용자의 이메일
        // 멤버 엔티티 조회는 서비스 내에서 처리하도록 구현했다고 가정
        mealCommandService.createMeal(dto, email);
        return CustomResponse.onSuccess(null);
    }

    // 식단 수정
    @PutMapping("/{mealId}")
    @Operation(summary = "식단 수정", description = "본인이 등록한 식단을 수정합니다.")
    public CustomResponse<Void> updateMeal(
            @PathVariable Long mealId,
            @RequestBody @Valid MealReqDTO.MealReq dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        mealCommandService.updateMeal(mealId, dto, email);
        return CustomResponse.onSuccess(null);
    }

    // 식단 삭제
    @DeleteMapping("/{mealId}")
    @Operation(summary = "식단 삭제", description = "본인이 등록한 식단을 삭제합니다.")
    public CustomResponse<Void> deleteMeal(
            @PathVariable Long mealId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        mealCommandService.deleteMeal(mealId, email);
        return CustomResponse.onSuccess(null);
    }

    // 특정 날짜 식단 목록 조회
    @GetMapping
    @Operation(summary = "식단 조회", description = "특정 날짜에 등록된 회원 식단 목록을 조회합니다. 날짜는 쿼리 파라미터로 전달합니다.")
    public CustomResponse<List<MealResDTO.MealRes>> getMealsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        List<MealResDTO.MealRes> meals = mealQueryService.getMealsByDate(date, email);
        return CustomResponse.onSuccess(meals);
    }
}
