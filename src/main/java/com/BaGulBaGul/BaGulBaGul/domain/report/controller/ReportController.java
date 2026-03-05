package com.BaGulBaGul.BaGulBaGul.domain.report.controller;

import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.request.CommentChildReportRegisterAPIRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.request.CommentReportRegisterAPIRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.request.EventReportRegisterAPIRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.request.RecruitmentReportRegisterAPIRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.response.ReportRegisterApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;

public interface ReportController {
    ApiResponse<ReportRegisterApiResponse> registerEventReport(
            AuthenticatedUserInfo authenticatedUserInfo,
            EventReportRegisterAPIRequest eventReportRegisterAPIRequest
    );
    ApiResponse<ReportRegisterApiResponse> registerRecruitmentReport(
            AuthenticatedUserInfo authenticatedUserInfo,
            RecruitmentReportRegisterAPIRequest recruitmentReportRegisterAPIRequest
    );
    ApiResponse<ReportRegisterApiResponse> registerCommentReport(
            AuthenticatedUserInfo authenticatedUserInfo,
            CommentReportRegisterAPIRequest commentReportRegisterAPIRequest
    );
    ApiResponse<ReportRegisterApiResponse> registerCommentChildReport(
            AuthenticatedUserInfo authenticatedUserInfo,
            CommentChildReportRegisterAPIRequest commentChildReportRegisterAPIRequest
    );
}
