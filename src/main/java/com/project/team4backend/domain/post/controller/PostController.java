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

    @GetMapping
    @Operation(summary = "게시글 전체 조회 (페이지네이션)", description = "페이지네이션을 이용해 게시글 리스트를 조회합니다.")
    public CustomResponse<PostResDTO.PostPageResDTO> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC, "postId"));
        return CustomResponse.onSuccess(postQueryService.getAllPosts(pageable));
    }

    @GetMapping("/scraps")
    @Operation(summary = "내가 스크랩한 게시글 조회 (페이지네이션)", description = "로그인한 사용자가 스크랩한 게시글을 페이지네이션으로 조회합니다.")
    public CustomResponse<PostResDTO.PostPageResDTO> getMyScrapPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return CustomResponse.onSuccess(postQueryService.getScrappedPosts(pageable, email));
    }

    @GetMapping("/liked")
    @Operation(summary = "좋아요한 게시글 목록 조회", description = "로그인한 사용자가 좋아요한 게시글을 페이지네이션으로 조회합니다.")
    public CustomResponse<PostResDTO.PostPageResDTO> getLikedPosts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String email = userDetails.getUsername();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return CustomResponse.onSuccess(postQueryService.getLikedPosts(email, pageable));
    }

    @GetMapping("/my-comments")
    @Operation(summary = "댓글 단 게시글 목록 조회", description = "사용자가 댓글 단 게시글을 페이지네이션으로 조회합니다.")
    public CustomResponse<PostResDTO.PostPageResDTO> getCommentedPosts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String email = userDetails.getUsername();
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return CustomResponse.onSuccess(postQueryService.getCommentedPosts(email, pageable));
    }

    @GetMapping("/my")
    @Operation(summary = "내가 작성한 게시글 조회", description = "자신이 작성한 게시글을 페이지네이션으로 조회합니다.")
    public CustomResponse<PostResDTO.PostPageResDTO> getMyPosts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "postId"));
        String email = userDetails.getUsername();
        return CustomResponse.onSuccess(postQueryService.getMyPosts(email, pageable));
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

    @PostMapping("/{postId}/like")
    @Operation(summary = "게시글 좋아요 토글", description = "해당 게시글에 좋아요를 추가하거나 제거합니다.")
    public CustomResponse<PostResDTO.ToggleResDTO> toggleLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        return CustomResponse.onSuccess(postCommandService.toggleLike(postId, email));
    }

    @PostMapping("/{postId}/scrap")
    @Operation(summary = "게시글 스크랩 토글", description = "해당 게시글을 스크랩하거나 스크랩을 해제합니다.")
    public CustomResponse<PostResDTO.ToggleResDTO> toggleScrap(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails) {

        String email = userDetails.getUsername();
        return CustomResponse.onSuccess(postCommandService.toggleScrap(postId, email));
    }


}
