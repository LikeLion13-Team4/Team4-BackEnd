package com.project.team4backend.domain.post.entity;

import com.project.team4backend.domain.comment.entity.Comment;
import com.project.team4backend.domain.member.entity.Member;
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

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostImage> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag_name")
    @Enumerated(EnumType.STRING)
    private Set<PostTagType> tags = new HashSet<>(); // 태그 복수선택를 위해 Set으로 관리

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // 빌더 패턴 사용 시 기본값 설정을 위함
    private List<Comment> comments = new ArrayList<>();

    public void update(String title, String content, Set<PostTagType> tags, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.tags = tags;

        // 기존 이미지 비우고 새로 설정
        this.images.clear();
        if (imageUrls != null) {
            imageUrls.forEach(url -> this.images.add(PostImage.builder().post(this).imageUrl(url).build()));
        }
    }



}
