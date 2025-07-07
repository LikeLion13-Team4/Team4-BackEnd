package com.project.team4backend.domain.post.service.query;

import com.project.team4backend.domain.post.dto.reponse.PostResDTO;

public interface PostQueryService {
    PostResDTO.PostDetailResDTO getPostDetail(Long postId, String email);
}
