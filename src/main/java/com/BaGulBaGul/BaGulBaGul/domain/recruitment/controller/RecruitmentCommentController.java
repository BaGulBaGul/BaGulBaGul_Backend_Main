package com.BaGulBaGul.BaGulBaGul.domain.recruitment.controller;


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
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.RecruitmentIdApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentCommentController {
    ApiResponse<PostCommentDetailResponse> getCommentDetail(Long commentId);
    ApiResponse<Page<GetPostCommentPageResponse>> getRecruitmentCommentPage(
            Long recruitmentId,
            AuthenticatedUserInfo authenticatedUserInfo,
            Pageable pageable
    );
    ApiResponse<Page<GetPostCommentChildPageResponse>> getRecruitmentCommentChildPage(
            Long commentId,
            AuthenticatedUserInfo authenticatedUserInfo,
            Pageable pageable
    );
    ApiResponse<PostCommentRegisterResponse> registerComment(
            Long eventId,
            AuthenticatedUserInfo authenticatedUserInfo,
            PostCommentRegisterRequest postCommentRegisterRequest
    );
    ApiResponse<Object> modifyComment(
            Long commentId,
            AuthenticatedUserInfo authenticatedUserInfo,
            PostCommentModifyRequest postCommentModifyRequest
    );
    ApiResponse<Object> deleteComment(
            Long commentId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<PostCommentChildRegisterResponse> registerCommentChild(
            Long commentId,
            AuthenticatedUserInfo authenticatedUserInfo,
            PostCommentChildRegisterRequest postCommentChildRegisterRequest
    );
    ApiResponse<Object> modifyCommentChild(
            Long commentChildId,
            AuthenticatedUserInfo authenticatedUserInfo,
            PostCommentChildModifyRequest postCommentChildModifyRequest
    );
    ApiResponse<Object> deleteCommentChild(
            Long commentChildId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<LikeCountResponse> addLikeToComment(
            Long commentId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<LikeCountResponse> deleteLikeToComment(
            Long commentId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<IsMyLikeResponse> isMyLikeComment(
            Long commentId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<LikeCountResponse> addLikeToCommentChild(
            Long commentChildId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<LikeCountResponse> deleteLikeToCommentChild(
            Long commentChildId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<IsMyLikeResponse> isMyLikeCommentChild(
            Long commentChildId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<RecruitmentIdApiResponse> getRecruitmentIdFromCommentId(Long commentId);
}
