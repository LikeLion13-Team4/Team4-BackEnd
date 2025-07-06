package com.project.team4backend.domain.comment.repository;

import com.project.team4backend.domain.comment.entity.Comment;
import com.project.team4backend.domain.comment.entity.CommentLike;
import com.project.team4backend.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByMemberAndComment(Member member, Comment comment);
}
