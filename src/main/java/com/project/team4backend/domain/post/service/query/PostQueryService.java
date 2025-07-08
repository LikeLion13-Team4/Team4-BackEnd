package com.project.team4backend.domain.post.service.query;

import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import org.springframework.data.domain.Pageable;

public interface PostQueryService {
    PostResDTO.PostDetailResDTO getPostDetail(Long postId, String email);

    PostResDTO.PostPageResDTO getAllPosts(Pageable pageable);

    //스크랩한 목록 조회
    PostResDTO.PostPageResDTO getScrappedPosts(Pageable pageable, String email);

    //좋아요 목록 조회
    PostResDTO.PostPageResDTO getLikedPosts(String email, Pageable pageable);
}
