package com.project.team4backend.domain.comment.dto.request;

public class CommentReqDTO {
    public record CommentRequestDto(
            String comment,
            Long parentId,
            Long postId
    ) {}
}
