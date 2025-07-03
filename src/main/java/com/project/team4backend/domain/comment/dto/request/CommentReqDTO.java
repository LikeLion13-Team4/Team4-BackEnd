package com.project.team4backend.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

public class CommentReqDTO {

    // 댓글/대댓글 생성 요청 DTO , 부모댓글id나 게시글 id는 URL로 받는걸로
    @Builder
    public record CommentCreateReqDTO(
            @NotBlank(message = "댓글 내용은 필수입니다.")
            String comment
    ) {}

    // 댓글 수정 요청 DTO
    @Builder
    public record CommentUpdateReqDTO(
            @NotBlank(message = "수정할 댓글 내용은 필수입니다.")
            String comment
    ) {}

}
