package com.project.team4backend.domain.comment.service.query;

import com.project.team4backend.domain.comment.converter.CommentConverter;
import com.project.team4backend.domain.comment.dto.responese.CommentResDTO;
import com.project.team4backend.domain.comment.entity.Comment;
import com.project.team4backend.domain.comment.exception.CommentErrorCode;
import com.project.team4backend.domain.comment.exception.CommentException;
import com.project.team4backend.domain.comment.repository.CommentRepository;
import com.project.team4backend.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentQueryServiceImpl implements CommentQueryService {

    PostRepository postRepository;
    CommentRepository commentRepository;

    @Override
    public List<CommentResDTO.CommentResponseDTO> getCommentsByPost(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.POST_NOT_FOUND));

        List<Comment> comments = commentRepository.findAlignedCommentByPostId(postId);
        return comments.stream()
                .map(CommentConverter::toCommentResponseDTO)
                .collect(Collectors.toList());
    }
}
