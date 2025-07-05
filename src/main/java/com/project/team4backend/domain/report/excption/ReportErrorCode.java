package com.project.team4backend.domain.report.excption;

import com.project.team4backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;



@Getter
@AllArgsConstructor
public enum ReportErrorCode implements BaseErrorCode {

    EMPTY_MEAL_DATA(HttpStatus.NOT_FOUND, "REPORT_404_1", "지정한 기간 동안 식단 데이터가 없습니다."),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "REPORT_400", "시작일은 종료일보다 이전이어야 합니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND,"REPORT_404_2", " 보고서를 찾을 수 없습니다." ),
    GPT_CALL_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "GPT429", "오늘 GPT 호출 횟수를 초과했습니다. 내일 다시 시도해주세요."),
    GPT_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "GPT500", "GPT 피드백 생성에 실패했습니다.")
    ;
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
