package com.BaGulBaGul.BaGulBaGul.domain.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.LikeCountResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request.RecruitmentModifyApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request.RecruitmentPageApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request.RecruitmentRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.GetLikeRecruitmentApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.RecruitmentDetailApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.RecruitmentIdApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.RecruitmentPageApiResponse;
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
            Long userId,
            RecruitmentRegisterApiRequest recruitmentRegisterApiRequest
    );

    ApiResponse<Object> modifyRecruitment(
            Long recruitmentId,
            Long userId,
            RecruitmentModifyApiRequest recruitmentModifyApiRequest
    );
    ApiResponse<Object> deleteRecruitment(Long recruitmentId, Long userId);
    ApiResponse<LikeCountResponse> addLike(Long recruitmentId, Long userId);
    ApiResponse<LikeCountResponse> deleteLike(Long recruitmentId, Long userId);
    ApiResponse<IsMyLikeResponse> isMyLike(Long recruitmentId, Long userId);
    ApiResponse<Page<GetLikeRecruitmentApiResponse>> getMyLike(
            Long userId,
            Pageable pageable
    );
}
