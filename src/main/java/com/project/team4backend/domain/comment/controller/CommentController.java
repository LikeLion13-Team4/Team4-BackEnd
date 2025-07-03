package com.project.team4backend.domain.comment.controller;

import com.project.team4backend.domain.comment.dto.request.CommentReqDTO;
import com.project.team4backend.domain.comment.dto.responese.CommentResDTO;
import com.project.team4backend.domain.comment.service.command.CommentCommandService;
import com.project.team4backend.domain.comment.service.query.CommentQueryService;
import com.project.team4backend.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "댓글 API", description = "댓글 기능 관련 API입니다.")
public class CommentController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    // 게시글 댓글 생성
    @PostMapping("/posts/{postId}/comments")
    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    public CustomResponse<CommentResDTO.CommentCreateResDTO> createComment(
            @PathVariable Long postId,
            @RequestBody @Valid CommentReqDTO.CommentCreateReqDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        return CustomResponse.onSuccess(commentCommandService.createComment(postId, null, dto, userEmail));
    }

    // 대댓글 생성
    @PostMapping("/comments/{parentId}/replies")
    @Operation(summary = "대댓글 작성", description = "특정 댓글에 대댓글을 작성합니다.")
    public CustomResponse<CommentResDTO.CommentCreateResDTO> createReply(
            @PathVariable Long parentId,
            @RequestBody @Valid CommentReqDTO.CommentCreateReqDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        return CustomResponse.onSuccess(commentCommandService.createComment(null, parentId, dto, userEmail));
    }

    // 게시글 댓글 + 대댓글 조회
    @GetMapping("/posts/{postId}/comments")
    @Operation(summary = "댓글 목록 조회", description = "특정 게시글의 모든 댓글과 대댓글을 조회합니다.")
    public CustomResponse<List<CommentResDTO.CommentResponseDTO>> getCommentsByPost(
            @PathVariable Long postId) {

        return CustomResponse.onSuccess(commentQueryService.getCommentsByPost(postId));
    }

    // 댓글 수정
    @PutMapping("/comments/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다. (작성자만 가능)")
    public CustomResponse<CommentResDTO.CommentResponseDTO> updateComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid CommentReqDTO.CommentUpdateReqDTO dto) {

        String userEmail = userDetails.getUsername();
        return CustomResponse.onSuccess(commentCommandService.updateComment(commentId, userEmail, dto));
    }

    // 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글 ID로 댓글을 삭제합니다. (본인만 가능)")
    public CustomResponse<Void> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String userEmail = userDetails.getUsername();
        commentCommandService.deleteComment(commentId, userEmail);
        return CustomResponse.onSuccess(null);
    }

    // 댓글 좋아요
    @PostMapping("/comments/{commentId}/like")
    @Operation(summary = "댓글 좋아요", description = "댓글 ID를 기준으로 좋아요를 누릅니다.")
    public CustomResponse<Void> likeComment(@PathVariable Long commentId) {
        commentCommandService.likeComment(commentId);
        return CustomResponse.onSuccess(null);
    }
}
