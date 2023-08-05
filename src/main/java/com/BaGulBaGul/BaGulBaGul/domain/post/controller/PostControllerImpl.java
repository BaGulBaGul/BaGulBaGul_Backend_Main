package com.BaGulBaGul.BaGulBaGul.domain.post.controller;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostServiceImpl;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostControllerImpl implements PostController {
    private final PostServiceImpl postService;

    @GetMapping("/{postId}")
    public ApiResponse<PostDetailResponse> getPostById(
            @PathVariable(name="postId") Long postId
    ) {
        PostDetailResponse postDetailResponse = postService.getPostDetailById(postId);
        return ApiResponse.of(postDetailResponse);
    }

    @GetMapping("/")
    public ApiResponse<Page<PostSimpleResponse>> getPostPageByCondition(
            PostConditionalRequest postConditionalRequest,
            Pageable pageable
    ) {
        return ApiResponse.of(
                postService.getPostPageByCondition(postConditionalRequest, pageable)
        );
    }

}
