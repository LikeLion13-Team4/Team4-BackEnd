package com.project.team4backend.domain.post.service.command;

import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import com.project.team4backend.domain.post.dto.request.PostReqDTO;


public interface PostCommandService {
    PostResDTO.PostCreateResDTO createPost(PostReqDTO.PostCreateReqDTO dto, String Email);


    PostResDTO.PostUpdateResDTO updatePost(Long postId, PostReqDTO.PostUpdateReqDTO dto, String email);

    PostResDTO.PostDeleteResDTO deletePost(Long postId, String email);


    PostResDTO.ToggleResDTO toggleLike(Long postId, String email);

    PostResDTO.ToggleResDTO toggleScrap(Long postId, String email);
}
