package com.BaGulBaGul.BaGulBaGul.domain.post.controller;


import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCommentController {
    ApiResponse<Page<GetPostCommentPageResponse>> getPostCommentPage(Long postId, Long requestUserId, Pageable pageable);
}
