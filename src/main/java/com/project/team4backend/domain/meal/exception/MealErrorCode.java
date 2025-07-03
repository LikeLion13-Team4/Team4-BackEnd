package com.project.team4backend.domain.meal.exception;

import com.project.team4backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MealErrorCode implements BaseErrorCode {
    MEAL_NOT_FOUND(HttpStatus.NOT_FOUND, "MEAL_001", "해당 식단을 찾을 수 없습니다."),
    INVALID_MEAL_DATE(HttpStatus.BAD_REQUEST, "MEAL_002", "유효하지 않은 날짜입니다."),
    DUPLICATE_MEAL(HttpStatus.CONFLICT, "MEAL_003", "이미 해당 날짜에 식단이 등록되어 있습니다."),
    UNAUTHORIZED_MEAL_ACCESS(HttpStatus.FORBIDDEN, "MEAL_004", "해당 식단에 대한 접근 권한이 없습니다."),
    NUTRITION_INFO_MISSING(HttpStatus.BAD_REQUEST, "MEAL_005", "영양 정보가 누락되었습니다."),
    MEAL_CHECK_ALREADY_DONE(HttpStatus.CONFLICT, "MEAL_006", "이미 출석 체크가 완료된 날짜입니다."),
    MEAL_CHECK_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MEAL_007", "출석 체크 중 오류가 발생했습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
