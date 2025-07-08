package com.project.team4backend.domain.post.entity;

import com.project.team4backend.domain.comment.entity.Comment;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.post.dto.request.PostReqDTO;
import com.project.team4backend.domain.post.enums.PostTagType;
import com.project.team4backend.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Builder.Default
    private Integer likeCount = 0;

    @Column(nullable = false)
    @Builder.Default
    private Integer scrapCount = 0;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostImage> images = new ArrayList<>();


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag_name")
    @Enumerated(EnumType.STRING)
    private Set<PostTagType> tags = new HashSet<>(); // 태그 복수선택를 위해 Set으로 관리


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // 빌더 패턴 사용 시 기본값 설정을 위함
    private List<Comment> comments = new ArrayList<>();


    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount = Math.max(0, this.likeCount - 1);
    }

    public void increaseScrapCount() {
        this.scrapCount++;
    }

    public void decreaseScrapCount() {
        this.scrapCount = Math.max(0, this.scrapCount - 1);
    }

    public void addImage(PostImage image) {
        this.images.add(image);
        image.updatePost(this); // 양쪽 다 맞춰줌 양방향 동기화 매서드
    }

    public void update(String title, String content, Set<PostTagType> tags, List<PostImage> newImages) {
        this.title = title;
        this.content = content;
        this.tags = tags;

        this.images.clear();
        if (newImages != null) {
            newImages.forEach(this::addImage); // 양방향 연관관계 유지
        }
    }
}
