package com.BaGulBaGul.BaGulBaGul.domain.post.controller;


import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentChildPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCommentController {
    ApiResponse<Page<GetPostCommentPageResponse>> getPostCommentPage(Long postId, Long requestUserId, Pageable pageable);
    ApiResponse<Page<GetPostCommentChildPageResponse>> getPostCommentChildPage(Long postCommentId, Long requestUserId, Pageable pageable);
    ApiResponse<PostCommentRegisterResponse> registerPostComment(Long postId, Long userId, PostCommentRegisterRequest postCommentRegisterRequest);
    ApiResponse<PostCommentChildRegisterResponse> registerPostCommentChild(Long postCommentId, Long userId, PostCommentChildRegisterRequest postCommentChildRegisterRequest);
}
