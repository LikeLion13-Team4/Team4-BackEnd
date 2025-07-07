package com.project.team4backend.domain.post.exception;

import com.project.team4backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class PostException extends CustomException {
    public PostException(PostErrorCode errorCode) {
        super(errorCode);
    }
}
