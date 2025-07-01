package com.project.team4backend.domain.post.entity;

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
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(length = 100)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "like_count")
    private Integer likeCount;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
