package com.project.team4backend.domain.post.controller;

import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import com.project.team4backend.domain.post.dto.request.PostReqDTO;
import com.project.team4backend.domain.post.service.command.PostCommandService;
import com.project.team4backend.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Tag(name = "게시글 API", description = "게시글 등록, 수정, 삭제 관련 API입니다.")
public class PostController {

    private final PostCommandService postCommandService;

    @PostMapping
    @Operation(summary = "게시글 등록", description = "회원이 새로운 게시글을 등록합니다.")
    public CustomResponse<PostResDTO.PostCreateResDTO> createPost(
            @RequestBody @Valid PostReqDTO.PostCreateReqDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        PostResDTO.PostCreateResDTO response = postCommandService.createPost(dto, email);
        return CustomResponse.onSuccess(response);
    }
}
