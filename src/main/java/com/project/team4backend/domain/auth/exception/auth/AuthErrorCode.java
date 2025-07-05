package com.project.team4backend.domain.auth.exception.auth;

import com.project.team4backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCode implements BaseErrorCode {

    AUTH_INVALID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_400_1", "잘못된 요청입니다."),
    AUTH_SAME_PASSWORD(HttpStatus.BAD_REQUEST, "AUTH400_2", "현재비밀번호와 새비밀번호가 같습니다."),
    AUTH_WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "AUTH401_1", "비밀번호가 일치하지 않습니다."),
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_402_2", "인증이 필요합니다"),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH403", "접근이 금지되었습니다"),
    AUTH_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_404","찾을 수 없습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
