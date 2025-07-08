package com.project.team4backend.domain.post.repository;

import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.post.entity.Post;
import com.project.team4backend.domain.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostAndMember(Post post, Member member);

    Optional<PostLike> findByPostAndMember(Post post, Member member);

    int countByPost(Post post);


}
