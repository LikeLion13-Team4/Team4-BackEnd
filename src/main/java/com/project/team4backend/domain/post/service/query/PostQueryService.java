package com.project.team4backend.domain.post.service.query;

import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import com.project.team4backend.domain.post.enums.PostTagType;
import org.springframework.data.domain.Pageable;

public interface PostQueryService {
    PostResDTO.PostDetailResDTO getPostDetail(Long postId, String email);

    PostResDTO.PostPageResDTO getAllPosts(Pageable pageable);

    PostResDTO.PostPageResDTO getAllSearchPosts(String keyword, Pageable pageable);

    PostResDTO.PostPageResDTO getAllPostsToTag(PostTagType tag, Pageable pageable);
}
