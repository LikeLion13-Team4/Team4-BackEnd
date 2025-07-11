package com.project.team4backend.domain.comment.converter;

import com.project.team4backend.domain.comment.dto.request.CommentReqDTO;
import com.project.team4backend.domain.comment.dto.response.CommentResDTO;
import com.project.team4backend.domain.comment.entity.Comment;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.post.entity.Post;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentConverter {

    //댓글 생성 ReqDTO를 엔티티로 변환
    public static Comment toComment(CommentReqDTO.CommentCreateReqDTO dto, Post post, Member member, Long hierarchy, Long groups, Long orders, Comment parentComment) {
        return Comment.builder()
                .comment(dto.comment())
                .hierarchy(hierarchy)
                .groups(groups)
                .orders(orders)
                .post(post)
                .member(member)
                .parentComment(parentComment)
                .build();
    }// likes는 @Builder.Default로 0L, createdAt, updatedAt은 BaseEntity에서 자동설정


    //Comment 엔티티를 CommentCreateResDTO로 변환
    public static CommentResDTO.CommentCreateResDTO toCommentCreateResDTO(Comment comment) {
        return CommentResDTO.CommentCreateResDTO.builder()
                .commentId(comment.getCommentId())
                .createdAt(comment.getCreatedAt())
                .build();
    }


    //Comment 엔티티를 댓글 CommentResDTO로 변환
    public static CommentResDTO.CommentResponseDTO toCommentResponseDTO(Comment comment) {
        return CommentResDTO.CommentResponseDTO.builder()
                .commentId(comment.getCommentId())
                .comment(comment.getComment())
                .hierarchy(comment.getHierarchy())
                .orders(comment.getOrders())
                .groups(comment.getGroups())
                .likes(comment.getLikes())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .postId(comment.getPost().getPostId())
                .memberId(comment.getMember().getId())
                .memberNickname(comment.getMember().getNickname())
                .build();
    }

    //CommentUpdateReqDTO의 내용으로 기존 Comment 엔티티를 업데이트
    //새로운 엔티티를 생성하는 것이 아닌, 기존 엔티티의 필드를 변경
    public static void updateComment(Comment comment, CommentReqDTO.CommentUpdateReqDTO dto) {
        comment.updateComment(dto.comment());
    }



}
