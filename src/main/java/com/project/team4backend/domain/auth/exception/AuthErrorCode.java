package com.project.team4backend.domain.auth.exception;

import com.project.team4backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    _INVALID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_402", "인증이 필요합니다"),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH403", "접근이 금지되었습니다"),
    _NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_404","찾을 수 없습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
