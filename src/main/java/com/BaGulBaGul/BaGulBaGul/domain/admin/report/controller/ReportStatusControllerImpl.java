package com.BaGulBaGul.BaGulBaGul.domain.admin.report.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.report.dto.api.request.FindReportStatusByConditionApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.report.dto.api.response.FindReportStatusByConditionApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.response.ReportApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.FindReportStatusByConditionResponse;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportInfo;
import com.BaGulBaGul.BaGulBaGul.domain.report.service.ReportService;
import com.BaGulBaGul.BaGulBaGul.domain.report.service.ReportStatusService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/report-status")
@Api(tags = "관리자 - 신고 상태 관리", description = "MANAGE_REPORT 권한 필요")
@PreAuthorize("hasAuthority('MANAGE_REPORT')")
public class ReportStatusControllerImpl implements ReportStatusController {

    private final ReportStatusService reportStatusService;
    private final ReportService reportService;

    @Override
    @GetMapping("/")
    @Operation(summary = "ReportStatus를 조건, 페이지 조회. ", description = "createdAt을 이용해 신고일시 기준 정렬")
    public ApiResponse<Page<FindReportStatusByConditionApiResponse>> findReportStatusByConditionAndPageable(
            FindReportStatusByConditionApiRequest apiRequest, Pageable pageable
    ) {
        Page<FindReportStatusByConditionResponse> serviceResponse = reportStatusService.findReportStatusByCondition(
                apiRequest.toServiceRequest(), pageable);
        return ApiResponse.of(
                serviceResponse.map(FindReportStatusByConditionApiResponse::from)
        );
    }

    @Override 
    @GetMapping("/{reportStatusId}/report/")
    @Operation(summary = "ReportStatus에 속한 Report를 페이지 조회")
    public ApiResponse<Page<ReportApiResponse>> findReportByReportStatusAndPageable(
            @PathVariable(name = "reportStatusId") Long reportStatusId,
            Pageable pageable
    ) {
        Page<ReportInfo> result = reportService.findByReportStatusIdAndPageable(reportStatusId, pageable);
        return ApiResponse.of(result.map(ReportApiResponse::from));
    }
}
