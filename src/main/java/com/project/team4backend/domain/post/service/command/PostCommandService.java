package com.project.team4backend.domain.post.service.command;

import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import com.project.team4backend.domain.post.dto.request.PostReqDTO;
import com.project.team4backend.domain.post.enums.PostTagType;

import java.util.List;
import java.util.Set;

public interface PostCommandService {
    PostResDTO.PostCreateResDTO createPost(PostReqDTO.PostCreateReqDTO dto, String Email);


    void updatePost(Long postId, PostReqDTO.PostUpdateReqDTO dto, String email);

    void deletePost(Long postId, String email);
}
