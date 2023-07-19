package com.BaGulBaGul.BaGulBaGul.domain.post.controller;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostServiceImpl;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
