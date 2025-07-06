package com.project.team4backend.domain.comment.exception;

import com.project.team4backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CommentErrorCode implements BaseErrorCode {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "댓글을 찾을 수 없습니다."),
    UNAUTHORIZED_COMMENT_DELETE(HttpStatus.FORBIDDEN, "C002", "본인의 댓글만 삭제할 수 있습니다."),
    UNAUTHORIZED_COMMENT_UPDATE(HttpStatus.FORBIDDEN, "C003", "본인의 댓글만 수정할 수 있습니다."),
    INVALID_COMMENT_CONTENT(HttpStatus.BAD_REQUEST, "C004", "댓글 내용은 비어 있을 수 없습니다."),
    PARENT_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "C005", "부모 댓글이 존재하지 않습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "C006", "댓글을 작성할 게시글을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "C007", "회원을 찾을 수 없습니다."),
    ALREADY_LIKED(HttpStatus.FORBIDDEN, "C008","좋아요는 한번만 가능합니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
