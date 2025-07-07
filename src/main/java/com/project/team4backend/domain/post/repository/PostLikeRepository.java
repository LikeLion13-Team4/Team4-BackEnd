package com.project.team4backend.domain.post.repository;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.post.entity.Post;
import com.project.team4backend.domain.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostAndMember(Post post, Member member); // 내가 스크랩 했는지

    int countByPost(Post post); // 해당 게시글 스크랩 수
}
