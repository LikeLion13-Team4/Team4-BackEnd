package com.project.team4backend.domain.comment.service.command;


import com.project.team4backend.domain.comment.converter.CommentConverter;
import com.project.team4backend.domain.comment.dto.request.CommentReqDTO;
import com.project.team4backend.domain.comment.dto.response.CommentResDTO;
import com.project.team4backend.domain.comment.entity.Comment;
import com.project.team4backend.domain.comment.entity.CommentLike;
import com.project.team4backend.domain.comment.exception.CommentErrorCode;
import com.project.team4backend.domain.comment.exception.CommentException;
import com.project.team4backend.domain.comment.repository.CommentLikeRepository;
import com.project.team4backend.domain.comment.repository.CommentRepository;
import com.project.team4backend.domain.member.entity.Member;
import com.project.team4backend.domain.member.exception.MemberErrorCode;
import com.project.team4backend.domain.member.exception.MemberException;
import com.project.team4backend.domain.member.repository.MemberRepository;
import com.project.team4backend.domain.post.entity.Post;
import com.project.team4backend.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentCommandServiceImpl implements CommentCommandService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    public CommentResDTO.CommentCreateResDTO createComment(Long postId, Long parentId, CommentReqDTO.CommentCreateReqDTO dto, String memberEmail) {
        // 멤버 조회
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new CommentException(CommentErrorCode.MEMBER_NOT_FOUND));

        // Post 객체 가져오기
        Post post;
        if (postId != null) {
            post = postRepository.findById(postId)
                    .orElseThrow(() -> new CommentException(CommentErrorCode.POST_NOT_FOUND));
        } else if (parentId != null) {
            Comment parentComment = commentRepository.findById(parentId)
                    .orElseThrow(() -> new CommentException(CommentErrorCode.PARENT_COMMENT_NOT_FOUND));
            post = parentComment.getPost();
        } else {
            throw new CommentException(CommentErrorCode.POST_NOT_FOUND);  // 또는 적절한 예외
        }

        Long hierarchy;
        Long groups;
        Long orders;

        if (parentId == null) {  // 댓글 작성
            hierarchy = 0L;
            groups = commentRepository.count() + 1;
            orders = 0L;
        } else {  // 대댓글 작성
            Comment parentComment = commentRepository.findById(parentId)
                    .orElseThrow(() -> new CommentException(CommentErrorCode.PARENT_COMMENT_NOT_FOUND));
            hierarchy = 1L;
            groups = parentComment.getGroups();
            Long maxOrders = commentRepository.findMaxCommentOrder(post.getPostId(), groups);
            orders = (maxOrders == null) ? 0L : maxOrders + 1;
        }

        Comment comment = CommentConverter.toComment(dto, post, member, hierarchy, groups, orders);
        Comment savedComment = commentRepository.save(comment);

        return CommentConverter.toCommentCreateResDTO(savedComment);
    }

    //댓글을 삭제(본인의 댓글만)
    @Override
    public void deleteComment(Long commentId, String memberEmail) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        // 작성자 검증: 이메일로 찾은 Member 객체의 ID와 비교
        Member requestingMember = memberRepository.findByEmail(memberEmail) // 이메일로 Member 찾기
                .orElseThrow(() -> new CommentException(CommentErrorCode.MEMBER_NOT_FOUND));

        if (!comment.getMember().getId().equals(requestingMember.getId())) { // 찾은 Member의 ID와 비교
            throw new IllegalArgumentException("자신이 작성한 댓글만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment); //하드삭제
    }

    //댓글 수정(본인것만)
    @Override
    public CommentResDTO.CommentResponseDTO updateComment(Long commentId, String memberEmail, CommentReqDTO.CommentUpdateReqDTO dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        // 작성자 검증: 이메일로 찾은 Member 객체의 ID와 비교
        Member requestingMember = memberRepository.findByEmail(memberEmail) // 이메일로 Member 찾기
                .orElseThrow(() -> new CommentException(CommentErrorCode.MEMBER_NOT_FOUND));

        if (!comment.getMember().getId().equals(requestingMember.getId())) { // 찾은 Member의 ID와 비교
            throw new IllegalArgumentException("자신이 작성한 댓글만 수정할 수 있습니다.");
        }

        CommentConverter.updateComment(comment, dto);

        return CommentConverter.toCommentResponseDTO(comment);
    }



    //좋아요 증가
    @Override
    public void likeComment(Long commentId, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        boolean alreadyLiked = commentLikeRepository.existsByMemberAndComment(member, comment);

        if (alreadyLiked) {
            throw new CommentException(CommentErrorCode.ALREADY_LIKED);
        }

        comment.increaseLikes();
        commentLikeRepository.save(new CommentLike(member, comment));
    }
}
