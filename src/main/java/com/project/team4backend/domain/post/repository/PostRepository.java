package com.project.team4backend.domain.post.repository;


import com.project.team4backend.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = {"images", "member"}) //이미지 주소,멤버 미리 끌어오기
    Optional<Post> findById(Long postId);

    //대량 조회를 위한 페이지 네이션
    Page<Post> findAllByOrderByPostIdDesc(Pageable pageable);
}
