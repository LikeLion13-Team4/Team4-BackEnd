package com.project.team4backend.domain.comment.repository;

import com.project.team4backend.domain.comment.entity.Comment;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //한 게시글의 모든 댓글과 대댓글을 groups 기준으로 묶고, orders 기준으로 정렬해서 트리 형태로 출력
    @EntityGraph(attributePaths = {"post","member"})
    @Query("select c from Comment c where c.post.postId = :id order by c.groups, c.orders") //해당 게시글에 달린 댓글만 조회
    List<Comment> findAlignedCommentByPostId(@Param("id") Long postId);

    //새로운 대댓글이 들어올 때, 기존 대댓글들 중에서 제일 뒤에 붙이기 위해 가장 큰 orders 값을 찾음
    @Query("select max(c.orders) from Comment c where c.post.postId = :id and c.groups = :groups")
    Long findMaxCommentOrder(@Param("id") Long postId, @Param("groups") Long groups);

    //내가 단 댓글 목록 조회, 같은 글에 여러 댓글을 달았을 경우 중복 제거
    @Query("SELECT p FROM Post p WHERE p.postId IN " +
            "(SELECT DISTINCT c.post.postId FROM Comment c WHERE c.member = :member) " +
            "ORDER BY p.createdAt DESC")
    Page<Post> findDistinctPostsByMember(@Param("member") Member member, Pageable pageable);


    List<Comment> findAllByPost(Post post);
}
