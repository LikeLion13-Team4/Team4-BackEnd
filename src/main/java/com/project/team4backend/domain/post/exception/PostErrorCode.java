package com.project.team4backend.domain.post.exception;

import com.project.team4backend.global.apiPayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostErrorCode implements BaseErrorCode {
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "게시글을 찾을 수 없습니다."),
    UNAUTHORIZED_POST_DELETE(HttpStatus.FORBIDDEN, "P002", "본인의 게시글만 삭제할 수 있습니다."),
    UNAUTHORIZED_POST_UPDATE(HttpStatus.FORBIDDEN, "P003", "본인의 게시글만 수정할 수 있습니다."),
    INVALID_POST_TITLE(HttpStatus.BAD_REQUEST, "P004", "게시글 제목은 비어 있을 수 없습니다."),
    INVALID_POST_CONTENT(HttpStatus.BAD_REQUEST, "P005", "게시글 내용은 비어 있을 수 없습니다."),
    INVALID_TAG_COUNT(HttpStatus.BAD_REQUEST, "P007", "태그는 최소 한 개 이상 선택해야 합니다."),
    POST_IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "P008", "게시글 이미지 업로드에 실패했습니다."),
    POST_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "P009", "게시글 이미지를 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
