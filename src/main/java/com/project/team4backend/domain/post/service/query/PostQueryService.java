package com.project.team4backend.domain.post.service.query;

import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import com.project.team4backend.domain.post.entity.enums.PostTagType;
import org.springframework.data.domain.Pageable;

public interface PostQueryService {
    PostResDTO.PostDetailResDTO getPostDetail(Long postId, String email);

    PostResDTO.PostPageResDTO getAllPosts(Pageable pageable);

    PostResDTO.PostPageResDTO getAllSearchPosts(String keyword, Pageable pageable);

    PostResDTO.PostPageResDTO getAllPostsToTag(PostTagType tag, Pageable pageable);

    // 스크랩한 목록 조회
    PostResDTO.PostPageWithoutCountResDTO getScrappedPosts(Pageable pageable, String email);

    // 좋아요 목록 조회
    PostResDTO.PostPageWithoutCountResDTO getLikedPosts(String email, Pageable pageable);

    // 댓글 단 게시글 목록 조회
    PostResDTO.PostPageWithoutCountResDTO getCommentedPosts(String email, Pageable pageable);

    // 내가 쓴 게시글 목록 조회
    PostResDTO.PostPageWithoutCountResDTO getMyPosts(String email, Pageable pageable);

}
