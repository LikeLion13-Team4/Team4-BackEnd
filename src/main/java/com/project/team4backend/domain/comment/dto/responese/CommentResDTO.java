package com.project.team4backend.domain.comment.dto.responese;

import lombok.Builder;

import java.time.LocalDateTime;

public class CommentResDTO {
    @Builder
    public record CommentCreateResDTO(
            Long commentId,
            LocalDateTime createdAt
    ) {}

    // 댓글 조회 응답 DTO (모든 정보가 필요한 경우)
    @Builder
    public record CommentResponseDTO(
            Long commentId,
            String comment,
            Long hierarchy,
            Long orders,
            Long groups,
            Long likes,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long postId,
            Long memberId,
            String memberNickname
    ) {}
}
