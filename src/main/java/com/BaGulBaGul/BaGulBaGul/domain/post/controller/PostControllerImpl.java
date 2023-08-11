package com.BaGulBaGul.BaGulBaGul.domain.post.controller;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostSimpleResponse;
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

}
