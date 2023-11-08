package com.BaGulBaGul.BaGulBaGul.domain.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.GetLikeRecruitmentResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.IsMyLikeResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentConditionalApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentRegisterResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentController {
    ApiResponse<RecruitmentDetailResponse> getRecruitmentById(Long recruitmentId);

    ApiResponse<Page<RecruitmentSimpleResponse>> getRecruitmentPageByCondition(
            Long eventId,
            RecruitmentConditionalApiRequest recruitmentConditionalRequest,
            Pageable pageable
    );

    ApiResponse<RecruitmentRegisterResponse> registerRecruitment(
            Long eventId,
            Long userId,
            RecruitmentRegisterRequest recruitmentRegisterRequest
    );

    ApiResponse<Object> modifyRecruitment(
            Long recruitmentId,
            Long userId,
            RecruitmentModifyRequest recruitmentModifyRequest
    );
    ApiResponse<Object> deleteRecruitment(Long recruitmentId, Long userId);
    ApiResponse<Object> addLike(Long recruitmentId, Long userId);
    ApiResponse<Object> deleteLike(Long recruitmentId, Long userId);
    ApiResponse<IsMyLikeResponse> isMyLike(Long recruitmentId, Long userId);
    ApiResponse<Page<GetLikeRecruitmentResponse>> getMyLike(
            Long userId,
            Pageable pageable
    );
}
