package com.BaGulBaGul.BaGulBaGul.domain.recruitment.controller;


import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentChildPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.LikeCountResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentChildRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostCommentRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentCommentController {
    ApiResponse<PostCommentDetailResponse> getCommentDetail(Long commentId);
    ApiResponse<Page<GetPostCommentPageResponse>> getRecruitmentCommentPage(Long recruitmentId, Long requestUserId, Pageable pageable);
    ApiResponse<Page<GetPostCommentChildPageResponse>> getRecruitmentCommentChildPage(Long commentId, Long requestUserId, Pageable pageable);
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
}
