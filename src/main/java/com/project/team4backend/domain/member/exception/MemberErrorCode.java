package com.project.team4backend.domain.member.exception;

import com.project.team4backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MemberErrorCode implements BaseErrorCode {

    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER404_1", "회원을 찾을 수 없습니다."),
    MEMBER_EMAIL_DUPLICATE(HttpStatus.CONFLICT, "MEMBER409_1", "이미 사용 중인 이메일입니다."),
    MEMBER_WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "MEMBER401_1", "비밀번호가 일치하지 않습니다."),
    MEMBER_SAME_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER400_1", "현재비밀번호와 새비밀번호가 같습니다."),
    MEMBER_WRONG_VALUE(HttpStatus.BAD_REQUEST, "MEMBER401_2", "잘못된 값을 입력했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
