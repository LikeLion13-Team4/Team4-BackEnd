package com.project.team4backend.domain.post.controller;

import com.project.team4backend.domain.image.dto.request.ImageReqDTO;
import com.project.team4backend.domain.image.dto.response.ImageResDTO;
import com.project.team4backend.domain.image.service.command.ImageCommandService;
import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import com.project.team4backend.domain.post.dto.request.PostReqDTO;
import com.project.team4backend.domain.post.service.command.PostCommandService;
import com.project.team4backend.domain.post.service.query.PostQueryService;
import com.project.team4backend.global.apiPayload.CustomResponse;
import com.project.team4backend.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Tag(name = "게시글 API", description = "게시글 등록, 수정, 삭제 관련 API입니다.")
public class PostController {

    private final PostCommandService postCommandService;
    private final PostQueryService postQueryService;
    private final ImageCommandService imageCommandService;

    @PostMapping
    @Operation(summary = "게시글 등록", description = "회원이 새로운 게시글을 등록합니다., 이미지 첨부 안할거면 images: []로 dto 제출하면 된다.")
    public CustomResponse<PostResDTO.PostCreateResDTO> createPost(
            @RequestBody @Valid PostReqDTO.PostCreateReqDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        PostResDTO.PostCreateResDTO response = postCommandService.createPost(dto, email);
        return CustomResponse.onSuccess(response);
    }

    @Operation(method = "POST", summary = "게시글 이미지 업로드", description = "게시글 이미지 선택 api, 업로드 하는건 아님, presignedUrl을 발급")
    @PostMapping("/postImage")
    public CustomResponse<ImageResDTO.PresignedUrlListResDTO> uploadPostImages
            (@AuthenticationPrincipal CustomUserDetails customUserDetails,
             @RequestBody ImageReqDTO.PresignedUrlListReqDTO presignedUrlListReqDTO) {
        ImageResDTO.PresignedUrlListResDTO presignedUrlListResDTO = imageCommandService.generatePresignedUrlList(customUserDetails.getEmail(), presignedUrlListReqDTO); // presignedUrl 발급
        return CustomResponse.onSuccess(presignedUrlListResDTO);
    }


    @GetMapping("/{postId}")
    @Operation(summary = "게시글 단건 조회", description = "특정 게시글을 상세 조회합니다.")
    public CustomResponse<PostResDTO.PostDetailResDTO> getPostDetail(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        PostResDTO.PostDetailResDTO res = postQueryService.getPostDetail(postId, email);
        return CustomResponse.onSuccess(res);
    }

    @PutMapping("/{postId}")
    @Operation(summary = "게시글 수정", description = "본인이 작성한 게시글을 수정합니다.")
    public CustomResponse<PostResDTO.PostUpdateResDTO> updatePost(
            @PathVariable Long postId,
            @RequestBody @Valid PostReqDTO.PostUpdateReqDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        PostResDTO.PostUpdateResDTO postUpdateResDTO = postCommandService.updatePost(postId, dto, email);
        return CustomResponse.onSuccess(postUpdateResDTO);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    public CustomResponse<PostResDTO.PostDeleteResDTO> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        PostResDTO.PostDeleteResDTO postDeleteResDTO = postCommandService.deletePost(postId, email);
        return CustomResponse.onSuccess(postDeleteResDTO);
    }
}
