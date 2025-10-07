package com.BaGulBaGul.BaGulBaGul.domain.admin.report.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.report.dto.api.request.FindReportStatusByConditionApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.report.dto.api.response.FindReportStatusByConditionApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.response.ReportApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportInfo;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportStatusController {
    ApiResponse<Page<FindReportStatusByConditionApiResponse>> findReportStatusByConditionAndPageable(
            FindReportStatusByConditionApiRequest apiRequest,
            Pageable pageable
    );
    ApiResponse<Page<ReportApiResponse>> findReportByReportStatusAndPageable(
            Long reportStatusId,
            Pageable pageable
    );
}
