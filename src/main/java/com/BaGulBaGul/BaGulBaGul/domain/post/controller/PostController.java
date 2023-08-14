package com.BaGulBaGul.BaGulBaGul.domain.post.controller;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetLikePostRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetLikePostResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostController {
    ApiResponse<PostDetailResponse> getPostById(Long postId);
    ApiResponse<Page<PostSimpleResponse>> getPostPageByCondition(PostConditionalRequest postConditionalRequest, Pageable pageable);
    ApiResponse<PostRegisterResponse> registerPost(Long userId, PostRegisterRequest postRegisterRequest);
    ApiResponse<Object> modifyPost(Long postId, Long userId, PostModifyRequest postModifyRequest);

    ApiResponse<Object> deletePost(Long postId, Long userId);

    ApiResponse<Object> addLike(Long postId, Long userId);
    ApiResponse<Object> deleteLike(Long postId, Long userId);
    ApiResponse<IsMyLikeResponse> isMyLike(Long postId, Long userId);
    ApiResponse<Page<GetLikePostResponse>> getMyLike(Long userId, GetLikePostRequest getLikePostRequest, Pageable pageable);
}
