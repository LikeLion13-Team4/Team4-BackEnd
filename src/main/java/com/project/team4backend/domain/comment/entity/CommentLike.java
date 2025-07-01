package com.project.team4backend.domain.comment.entity;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;
}
