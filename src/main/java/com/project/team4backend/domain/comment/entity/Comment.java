package com.project.team4backend.domain.comment.entity;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.post.entity.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @NotEmpty
    private String comment;

    private Long hierarchy; // 댓글이면 0 대댓글이면 1이 들어감
    private Long orders; // 대댓글 순서로 데이터 정렬할 때 필요
    private Long groups; // 한개의 댓글과 그에 딸린 대댓글들을 한 그룹으로 묶는다.
    private Long likes = 0L; // null방지를 위해 0으로 초기화
    private LocalDateTime createAt;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member user;


}
