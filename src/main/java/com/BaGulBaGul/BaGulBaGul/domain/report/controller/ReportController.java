package com.BaGulBaGul.BaGulBaGul.domain.report.controller;

import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.request.CommentChildReportRegisterAPIRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.request.CommentReportRegisterAPIRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.request.EventReportRegisterAPIRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.request.RecruitmentReportRegisterAPIRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.response.ReportRegisterApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;

public interface ReportController {
    ApiResponse<ReportRegisterApiResponse> registerEventReport(Long userId, EventReportRegisterAPIRequest eventReportRegisterAPIRequest);
    ApiResponse<ReportRegisterApiResponse> registerRecruitmentReport(Long userId, RecruitmentReportRegisterAPIRequest recruitmentReportRegisterAPIRequest);
    ApiResponse<ReportRegisterApiResponse> registerCommentReport(Long userId, CommentReportRegisterAPIRequest commentReportRegisterAPIRequest);
    ApiResponse<ReportRegisterApiResponse> registerCommentChildReport(Long userId, CommentChildReportRegisterAPIRequest commentChildReportRegisterAPIRequest);
}
