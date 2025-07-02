package com.project.team4backend.domain.member.exception;

import com.project.team4backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class MemberException extends CustomException {
    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }
}
