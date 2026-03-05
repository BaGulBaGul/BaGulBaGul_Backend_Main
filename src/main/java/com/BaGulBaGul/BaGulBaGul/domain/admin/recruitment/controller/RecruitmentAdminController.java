package com.BaGulBaGul.BaGulBaGul.domain.admin.recruitment.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.recruitment.dto.api.request.AdminRecruitmentPageApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent.QueryRecruitmentWithConditionByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.request.RecruitmentPageApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.RecruitmentPageApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.service.RecruitmentService;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.validation.ValidationUtil;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/event/recruitment")
@RequiredArgsConstructor
@Api(tags = "관리자 - 모집글 관리", description = "MANAGE_RECRUITMENT 권한 필요")
@PreAuthorize("hasAuthority('MANAGE_RECRUITMENT')")
public class RecruitmentAdminController {

    private final RecruitmentService recruitmentService;

    @GetMapping("/search")
    @Operation(summary = "주어진 조건에 맞는 모집글을 페이지 조회 - 관리자 버전",
            description = "일반 모집글 페이지 검색 api와 사용법이 같음"
    )
    public ApiResponse<Page<RecruitmentPageApiResponse>> getRecruitmentPageByCondition(
            AdminRecruitmentPageApiRequest adminRecruitmentPageApiRequest,
            Pageable pageable
    ) {
        //모집글 조건 조회 요청 생성 후 검증
        RecruitmentConditionalRequest recruitmentConditionalRequest = adminRecruitmentPageApiRequest.toRecruitmentConditionalRequest();
        ValidationUtil.validate(recruitmentConditionalRequest);
        //페이지 조회
        Page<RecruitmentSimpleResponse> recruitmentPageByCondition = recruitmentService.getRecruitmentPageByCondition(
                recruitmentConditionalRequest,
                pageable
        );
        //페이지 api 응답 dto로 변환
        Page<RecruitmentPageApiResponse> responses = recruitmentPageByCondition.map(RecruitmentPageApiResponse::from);
        return ApiResponse.of(responses);
    }

    @PostMapping("/deleted/{recruitmentId}/restore")
    @Operation(summary = "삭제된 모집글을 복구",
            description = "삭제된 모집글을 복구"
    )
    public ApiResponse<Void> restoreDeletedEvent(
            @AuthenticationPrincipal AuthenticatedUserInfo authenticatedUserInfo,
            @PathVariable(name = "recruitmentId") Long recruitmentId
    ) {
        recruitmentService.restoreRecruitment(authenticatedUserInfo, recruitmentId);
        return ApiResponse.of(null);
    }
}
