package com.project.team4backend.domain.auth.exception.email;

import com.project.team4backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EmailVerificationErrorCode implements BaseErrorCode {

    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "EmailVerification_404_1","이메일 정보를 찾을 수 없습니다"),
    EMAIL_NOT_FOUND_CODE(HttpStatus.NOT_FOUND, "EmailVerification_404_2", "인증 코드가 존재하지 않습니다."),
    EMAIL_BAD_REQUEST(HttpStatus.BAD_REQUEST, "EmailVerification_400_1", "인증 코드가 일치하지 않습니다."),
    EMAIL_EXPIRED(HttpStatus.BAD_REQUEST,"EmailVerification_400_2", "인증 코드가 만료되었습니다."),
    EMAIL_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST,"EmailVerification_400_3", "이미 인증이 완료되었습니다.");
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
