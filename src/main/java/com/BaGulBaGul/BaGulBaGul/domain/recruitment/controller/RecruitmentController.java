package com.BaGulBaGul.BaGulBaGul.domain.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.LikeCountResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request.RecruitmentModifyApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request.RecruitmentPageApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request.RecruitmentRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.RecruitmentDetailApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.RecruitmentIdApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.RecruitmentPageApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentController {
    ApiResponse<RecruitmentDetailApiResponse> getRecruitmentById(Long recruitmentId);

    ApiResponse<Page<RecruitmentPageApiResponse>> getRecruitmentPageByCondition(
            RecruitmentPageApiRequest recruitmentPageApiRequest,
            Pageable pageable
    );

    ApiResponse<RecruitmentIdApiResponse> registerRecruitment(
            Long eventId,
            AuthenticatedUserInfo authenticatedUserInfo,
            RecruitmentRegisterApiRequest recruitmentRegisterApiRequest
    );

    ApiResponse<Object> modifyRecruitment(
            Long recruitmentId,
            AuthenticatedUserInfo authenticatedUserInfo,
            RecruitmentModifyApiRequest recruitmentModifyApiRequest
    );
    ApiResponse<Object> deleteRecruitment(
            Long recruitmentId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<LikeCountResponse> addLike(
            Long recruitmentId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<LikeCountResponse> deleteLike(
            Long recruitmentId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<IsMyLikeResponse> isMyLike(
            Long recruitmentId,
            AuthenticatedUserInfo authenticatedUserInfo
    );
    ApiResponse<Page<RecruitmentPageApiResponse>> getMyLike(
            AuthenticatedUserInfo authenticatedUserInfo,
            Pageable pageable
    );
}
