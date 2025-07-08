package com.project.team4backend.domain.post.entity;

import com.project.team4backend.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Column(name = "image_url",length = 2048, nullable = false)
    private String imageUrl;

    @Column(name = "image_url_key", nullable = false)
    private String imageUrlKey;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    // 양방향 연관관계 편의용(동기화) setter
    public void updatePost(Post post) {
        this.post = post;
    }

}
