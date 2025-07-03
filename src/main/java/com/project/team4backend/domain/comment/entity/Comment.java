package com.project.team4backend.domain.comment.entity;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.post.entity.Post;
import com.project.team4backend.global.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @NotEmpty
    @Column(name = "comment")
    private String comment;

    @Column(name = "hierarchy")
    @Builder.Default
    private Long hierarchy = 0L; // 댓글이면 0 대댓글이면 1이 들어감

    @Column(name = "comment_orders")
    @Builder.Default
    private Long orders = 0L; // 대댓글 순서로 데이터 정렬할 때 필요

    @Column(name = "comment_groups")
    private Long groups; // 한개의 댓글과 그에 딸린 대댓글들을 한 그룹으로 묶는다.

    @Column(name = "likes")
    @Builder.Default
    private Long likes = 0L; // null방지를 위해 0으로 초기화

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // Comment 내용 업데이트 메서드
    public void updateComment(String newComment) {
        if (newComment == null || newComment.trim().isEmpty()) {
            throw new IllegalArgumentException("댓글 내용은 비어있을 수 없습니다.");
        }//null이거나 " "도 허용하지 않기 위해 trim().isEmpty(), 유효성검사
        this.comment = newComment;
    }
    // 좋아요 증가
    public void increaseLikes() {
        this.likes++;
    }
}

