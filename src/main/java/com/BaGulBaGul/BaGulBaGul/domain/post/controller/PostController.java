package com.BaGulBaGul.BaGulBaGul.domain.post.controller;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;

public interface PostController {
    ApiResponse<PostDetailResponse> getPostById(Long postId);
}
