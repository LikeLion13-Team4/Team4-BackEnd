package com.project.team4backend.domain.comment.service.query;

import com.project.team4backend.domain.comment.dto.responese.CommentResDTO;

import java.util.List;

public interface CommentQueryService {

    List<CommentResDTO.CommentResponseDTO> getCommentsByPost(Long postId);
}
