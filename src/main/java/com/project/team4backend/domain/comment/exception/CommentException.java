package com.project.team4backend.domain.comment.exception;

import com.project.team4backend.global.apiPayload.exception.CustomException;
import lombok.Getter;

@Getter
public class CommentException extends CustomException {
    public CommentException(CommentErrorCode errorCode) {super(errorCode);}
}
