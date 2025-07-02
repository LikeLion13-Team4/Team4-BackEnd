package com.project.team4backend.domain.comment.service.command;

import com.project.team4backend.domain.comment.dto.request.CommentReqDTO;
import com.project.team4backend.domain.comment.dto.responese.CommentResDTO;

public interface CommentCommandService {
    //댓글 등록
    CommentResDTO.CommentCreateResDTO createComment(Long postId, Long parentId, CommentReqDTO.CommentCreateReqDTO dto, String memberEmail);

    //댓글을 삭제(본인의 댓글만)
    void deleteComment(Long commentId, String memberEmail);

    //댓글 수정(본인것만)
    CommentResDTO.CommentResponseDTO updateComment(Long commentId, String memberEmail, CommentReqDTO.CommentUpdateReqDTO dto);

    //좋아요 증가
    void likeComment(Long commentId);
}
