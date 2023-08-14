package com.BaGulBaGul.BaGulBaGul.domain.post.controller;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetLikePostRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetLikePostResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostAPIService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostControllerImpl implements PostController {
    private final PostAPIService postAPIService;

    @Override
    @GetMapping("/{postId}")
    public ApiResponse<PostDetailResponse> getPostById(
            @PathVariable(name="postId") Long postId
    ) {
        PostDetailResponse postDetailResponse = postAPIService.getPostDetailById(postId);
        return ApiResponse.of(postDetailResponse);
    }

    @Override
    @GetMapping("")
    public ApiResponse<Page<PostSimpleResponse>> getPostPageByCondition(
            PostConditionalRequest postConditionalRequest,
            Pageable pageable
    ) {
        return ApiResponse.of(
                postAPIService.getPostPageByCondition(postConditionalRequest, pageable)
        );
    }

    @Override
    @PostMapping("")
    public ApiResponse<PostRegisterResponse> registerPost(Long userId, @RequestBody @Valid PostRegisterRequest postRegisterRequest) {
        Long postId = postAPIService.registerPost(userId, postRegisterRequest);
        return ApiResponse.of(
                new PostRegisterResponse(postId)
        );
    }

    @Override
    @PatchMapping("/{postId}")
    public ApiResponse<Object> modifyPost(
            @PathVariable(name="postId") Long postId,
            Long userId,
            @RequestBody PostModifyRequest postModifyRequest
    ) {
        postAPIService.modifyPost(postId, userId, postModifyRequest);
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/{postId}")
    public ApiResponse<Object> deletePost(
            @PathVariable(name="postId") Long postId,
            Long userId
    ) {
        postAPIService.deletePost(postId, userId);
        return ApiResponse.of(null);
    }

    @Override
    @PostMapping("/{postId}/addlike")
    public ApiResponse<Object> addLike(
            @PathVariable(name="postId") Long postId,
            Long userId
    ) {
        try {
            postAPIService.addLike(postId, userId);
        } catch (DuplicateLikeException duplicateLikeException) {
        }
        return ApiResponse.of(null);
    }

    @Override
    @DeleteMapping("/{postId}/deletelike")
    public ApiResponse<Object> deleteLike(
            @PathVariable(name="postId") Long postId,
            Long userId
    ) {
        try {
            postAPIService.deleteLike(postId, userId);
        } catch (LikeNotExistException e) {
        }
        return ApiResponse.of(null);
    }

    @Override
    @GetMapping("/{postId}/ismylike")
    public ApiResponse<IsMyLikeResponse> isMyLike(
            @PathVariable(name="postId") Long postId,
            Long userId
    ) {
        return ApiResponse.of(
                new IsMyLikeResponse(postAPIService.isMyLike(postId, userId))
        );
    }

    @Override
    @GetMapping("/mylike")
    public ApiResponse<Page<GetLikePostResponse>> getMyLike(
            Long userId,
            GetLikePostRequest getLikePostRequest,
            Pageable pageable
    ) {
        return ApiResponse.of(
                postAPIService.getMyLikePost(getLikePostRequest, userId, pageable)
        );
    }
}
