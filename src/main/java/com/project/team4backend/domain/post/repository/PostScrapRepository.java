package com.project.team4backend.domain.post.repository;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.post.entity.Post;
import com.project.team4backend.domain.post.entity.PostScrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostScrapRepository extends JpaRepository<PostScrap, Long> {
    boolean existsByPostAndMember(Post post, Member member); // 내가 좋아요 했는지

    int countByPost(Post post); // 해당 게시글 좋아요 수
}
