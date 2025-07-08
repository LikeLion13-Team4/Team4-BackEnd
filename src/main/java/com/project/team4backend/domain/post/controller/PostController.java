package com.project.team4backend.domain.post.controller;

import com.project.team4backend.domain.post.dto.reponse.PostResDTO;
import com.project.team4backend.domain.post.dto.request.PostReqDTO;
import com.project.team4backend.domain.post.service.command.PostCommandService;
import com.project.team4backend.domain.post.service.query.PostQueryService;
import com.project.team4backend.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @PostMapping
    @Operation(summary = "게시글 등록", description = "회원이 새로운 게시글을 등록합니다.")
    public CustomResponse<PostResDTO.PostCreateResDTO> createPost(
            @RequestBody @Valid PostReqDTO.PostCreateReqDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        PostResDTO.PostCreateResDTO response = postCommandService.createPost(dto, email);
        return CustomResponse.onSuccess(response);
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

    @GetMapping("/posts")
    @Operation(summary = "게시글 전체 조회 (페이지네이션)", description = "페이지네이션을 이용해 게시글 리스트를 조회합니다.")
    public CustomResponse<PostResDTO.PostPageResDTO> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC, "postId"));
        return CustomResponse.onSuccess(postQueryService.getAllPosts(pageable));
    }

    @PutMapping("/{postId}")
    @Operation(summary = "게시글 수정", description = "본인이 작성한 게시글을 수정합니다.")
    public CustomResponse<Void> updatePost(
            @PathVariable Long postId,
            @RequestBody @Valid PostReqDTO.PostUpdateReqDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        postCommandService.updatePost(postId, dto, email);
        return CustomResponse.onSuccess(null);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    public CustomResponse<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        postCommandService.deletePost(postId, email);

        return CustomResponse.onSuccess(null);
    }


}
