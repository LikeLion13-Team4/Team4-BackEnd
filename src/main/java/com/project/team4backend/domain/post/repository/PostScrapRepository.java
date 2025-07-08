package com.project.team4backend.domain.post.repository;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.post.entity.Post;
import com.project.team4backend.domain.post.entity.PostLike;
import com.project.team4backend.domain.post.entity.PostScrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostScrapRepository extends JpaRepository<PostScrap, Long> {
    boolean existsByPostAndMember(Post post, Member member);

    Optional<PostScrap> findByPostAndMember(Post post, Member member);

    int countByPost(Post post);
}
