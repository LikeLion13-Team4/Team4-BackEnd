package com.project.team4backend.domain.comment.repository;

import com.project.team4backend.domain.comment.entity.Comment;
import com.project.team4backend.domain.comment.entity.CommentLike;
import com.project.team4backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    void deleteAllByCommentIn(List<Comment> comments);

    boolean existsByMemberAndComment(Member member, Comment comment);
}
