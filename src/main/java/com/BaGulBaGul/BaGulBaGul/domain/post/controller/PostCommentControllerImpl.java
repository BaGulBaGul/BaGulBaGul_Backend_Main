package com.BaGulBaGul.BaGulBaGul.domain.post.controller;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentChildPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostCommentAPIService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/comment/{postCommentId}/postCommentChild")
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
}