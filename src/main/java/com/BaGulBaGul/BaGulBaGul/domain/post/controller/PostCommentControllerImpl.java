package com.BaGulBaGul.BaGulBaGul.domain.post.controller;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentChildPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostCommentAPIService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostCommentControllerImpl implements PostCommentController {

    private final PostCommentAPIService postCommentAPIService;

    @GetMapping("/{postId}/comment")
    @Override
    public ApiResponse<Page<GetPostCommentPageResponse>> getPostCommentPage(
            @PathVariable(name = "postId") Long postId,
            Long requestUserId,
            Pageable pageable
    ) {
        return ApiResponse.of(postCommentAPIService.getPostCommentPage(postId, requestUserId, pageable));
    }

    @GetMapping("/comment/{postCommentId}/children")
    @Override
    public ApiResponse<Page<GetPostCommentChildPageResponse>> getPostCommentChildPage(
            @PathVariable(name = "postCommentId") Long postCommentId,
            Long requestUserId,
            Pageable pageable
    ) {
        return ApiResponse.of(
                postCommentAPIService.getPostCommentChildPage(
                        postCommentId, requestUserId, pageable
                )
        );
    }

    @PostMapping("/{postId}/comment")
    @Override
    public ApiResponse<PostCommentRegisterResponse> registerPostComment(
            @PathVariable(name = "postId") Long postId,
            Long userId,
            @RequestBody @Valid PostCommentRegisterRequest postCommentRegisterRequest
    ) {
        Long postCommentId = postCommentAPIService.registerPostComment(postId, userId, postCommentRegisterRequest);
        return ApiResponse.of(
                new PostCommentRegisterResponse(postCommentId)
        );
    }

    @PatchMapping("comment/{postCommentId}")
    @Override
    public ApiResponse<Object> modifyPostComment(
            @PathVariable(name = "postCommentId") Long postCommentId,
            Long userId,
            @RequestBody PostCommentModifyRequest postCommentModifyRequest
    ) {
        postCommentAPIService.modifyPostComment(postCommentId, userId, postCommentModifyRequest);
        return ApiResponse.of(null);
    }

    @DeleteMapping("comment/{postCommentId}")
    @Override
    public ApiResponse<Object> deletePostComment(
            @PathVariable(name = "postCommentId") Long postCommentId,
            Long userId
    ) {
        postCommentAPIService.deletePostComment(postCommentId, userId);
        return ApiResponse.of(null);
    }

    @PostMapping("/comment/{postCommentId}/children")
    @Override
    public ApiResponse<PostCommentChildRegisterResponse> registerPostCommentChild(
            @PathVariable(name = "postCommentId") Long postCommentId,
            Long userId,
            @RequestBody @Valid PostCommentChildRegisterRequest postCommentChildRegisterRequest
    ) {
        Long postCommentChildId = postCommentAPIService.registerPostCommentChild(postCommentId, userId, postCommentChildRegisterRequest);
        return ApiResponse.of(
                new PostCommentChildRegisterResponse(postCommentChildId)
        );
    }

    @PatchMapping("/comment/children/{postCommentChildId}")
    @Override
    public ApiResponse<Object> modifyPostCommentChild(
            @PathVariable(name = "postCommentChildId") Long postCommentChildId,
            Long userId,
            @RequestBody PostCommentChildModifyRequest postCommentChildModifyRequest
    ) {
        postCommentAPIService.modifyPostCommentChild(postCommentChildId, userId, postCommentChildModifyRequest);
        return ApiResponse.of(null);
    }

    @DeleteMapping("/comment/children/{postCommentChildId}")
    @Override
    public ApiResponse<Object> deletePostCommentChild(
            @PathVariable(name = "postCommentChildId") Long postCommentChildId,
            Long userId
    ) {
        postCommentAPIService.deletePostCommentChild(postCommentChildId, userId);
        return ApiResponse.of(null);
    }

    @PostMapping("comment/{postCommentId}/like")
    @Override
    public ApiResponse<Object> addLikeToComment(
            @PathVariable(name = "postCommentId") Long postCommentId,
            Long userId
    ) {
        try {
            postCommentAPIService.addLikeToComment(postCommentId, userId);
        }
        catch (DuplicateLikeException duplicateLikeException) {
        }
        return ApiResponse.of(null);
    }


    @DeleteMapping("comment/{postCommentId}/like")
    @Override
    public ApiResponse<Object> deleteLikeToComment(
            @PathVariable(name = "postCommentId") Long postCommentId,
            Long userId
    ) {
        try {
            postCommentAPIService.deleteLikeToComment(postCommentId, userId);
        }
        catch (LikeNotExistException likeNotExistException) {
        }
        return ApiResponse.of(null);
    }

    @PostMapping("comment/children/{postCommentChildId}/like")
    @Override
    public ApiResponse<Object> addLikeToCommentChild(
            @PathVariable(name = "postCommentChildId") Long postCommentChildId,
            Long userId
    ) {
        try {
            postCommentAPIService.addLikeToCommentChild(postCommentChildId, userId);
        }
        catch (DuplicateLikeException duplicateLikeException) {
        }
        return ApiResponse.of(null);
    }

    @DeleteMapping("comment/children/{postCommentChildId}/like")
    @Override
    public ApiResponse<Object> deleteLikeToCommentChild(
            @PathVariable(name = "postCommentChildId") Long postCommentChildId,
            Long userId
    ) {
        try {
            postCommentAPIService.deleteLikeToCommentChild(postCommentChildId, userId);
        }
        catch (LikeNotExistException likeNotExistException) {
        }
        return ApiResponse.of(null);
    }
}