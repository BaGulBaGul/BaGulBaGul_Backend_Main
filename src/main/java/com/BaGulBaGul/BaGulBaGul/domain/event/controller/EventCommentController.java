package com.BaGulBaGul.BaGulBaGul.domain.event.controller;


import com.BaGulBaGul.BaGulBaGul.domain.event.dto.EventIdResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentChildPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.LikeCountResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostCommentChildRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostCommentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostCommentRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventCommentController {
    ApiResponse<PostCommentDetailResponse> getCommentDetail(Long commentId);
    ApiResponse<Page<GetPostCommentPageResponse>> getEventCommentPage(Long eventId, Long requestUserId, Pageable pageable);
    ApiResponse<Page<GetPostCommentChildPageResponse>> getEventCommentChildPage(Long commentId, Long requestUserId, Pageable pageable);
    ApiResponse<PostCommentRegisterResponse> registerComment(Long eventId, Long userId, PostCommentRegisterRequest postCommentRegisterRequest);
    ApiResponse<Object> modifyComment(Long commentId, Long userId, PostCommentModifyRequest postCommentModifyRequest);
    ApiResponse<Object> deleteComment(Long commentId, Long userId);
    ApiResponse<PostCommentChildRegisterResponse> registerCommentChild(Long commentId, Long userId, PostCommentChildRegisterRequest postCommentChildRegisterRequest);
    ApiResponse<Object> modifyCommentChild(Long commentChildId, Long userId, PostCommentChildModifyRequest postCommentChildModifyRequest);
    ApiResponse<Object> deleteCommentChild(Long commentChildId, Long userId);
    ApiResponse<LikeCountResponse> addLikeToComment(Long commentId, Long userId);
    ApiResponse<LikeCountResponse> deleteLikeToComment(Long commentId, Long userId);
    ApiResponse<IsMyLikeResponse> isMyLikeComment(Long commentId, Long userId);
    ApiResponse<LikeCountResponse> addLikeToCommentChild(Long commentChildId, Long userId);
    ApiResponse<LikeCountResponse> deleteLikeToCommentChild(Long commentChildId, Long userId);
    ApiResponse<IsMyLikeResponse> isMyLikeCommentChild(Long commentChildId, Long userId);
    ApiResponse<EventIdResponse> getEventIdFromCommentId(Long commentId);
}
