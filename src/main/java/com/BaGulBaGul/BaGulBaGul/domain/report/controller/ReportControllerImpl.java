package com.BaGulBaGul.BaGulBaGul.domain.report.controller;

import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.request.CommentChildReportRegisterAPIRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.request.CommentReportRegisterAPIRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.request.EventReportRegisterAPIRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.request.RecruitmentReportRegisterAPIRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.response.ReportRegisterApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.report.service.ReportService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
@Api(tags = "신고")
public class ReportControllerImpl implements ReportController {

    private final ReportService reportService;

    @Override
    @PostMapping("/event")
    @Operation(summary = "이벤트를 신고하는 api",
            description = "로그인 필수, 중복신고불가"
    )
    public ApiResponse<ReportRegisterApiResponse> registerEventReport(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody EventReportRegisterAPIRequest eventReportRegisterAPIRequest
    ) {
        Long eventId = eventReportRegisterAPIRequest.getEventId();
        Long reportId = reportService.registerEventReport(
                eventId,
                eventReportRegisterAPIRequest.toReportRegisterAPIRequest(userId));
        return ApiResponse.of(
                ReportRegisterApiResponse.builder()
                        .reportId(reportId)
                        .build());
    }

    @Override
    @PostMapping("/recruitment")
    @Operation(summary = "모집글을 신고하는 api",
            description = "로그인 필수, 중복신고불가"
    )
    public ApiResponse<ReportRegisterApiResponse> registerRecruitmentReport(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody RecruitmentReportRegisterAPIRequest recruitmentReportRegisterAPIRequest
    ) {
        Long recruitmentId = recruitmentReportRegisterAPIRequest.getRecruitmentId();
        Long reportId = reportService.registerRecruitmentReport(
                recruitmentId,
                recruitmentReportRegisterAPIRequest.toReportRegisterAPIRequest(userId));
        return ApiResponse.of(
                ReportRegisterApiResponse.builder()
                        .reportId(reportId)
                        .build());
    }

    @Override
    @PostMapping("/comment")
    @Operation(summary = "댓글을 신고하는 api",
            description = "로그인 필수, 중복신고불가"
    )
    public ApiResponse<ReportRegisterApiResponse> registerCommentReport(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CommentReportRegisterAPIRequest commentReportRegisterAPIRequest
    ) {
        Long postCommentId = commentReportRegisterAPIRequest.getCommentId();
        Long reportId = reportService.registerPostCommentReport(
                postCommentId,
                commentReportRegisterAPIRequest.toReportRegisterAPIRequest(userId));
        return ApiResponse.of(
                ReportRegisterApiResponse.builder()
                        .reportId(reportId)
                        .build());
    }

    @Override
    @PostMapping("/comment-child")
    @Operation(summary = "대댓글을 신고하는 api",
            description = "로그인 필수, 중복신고불가"
    )
    public ApiResponse<ReportRegisterApiResponse> registerCommentChildReport(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody CommentChildReportRegisterAPIRequest commentChildReportRegisterAPIRequest
    ) {
        Long postCommentChildId = commentChildReportRegisterAPIRequest.getCommentChildId();
        Long reportId = reportService.registerPostCommentChildReport(
                postCommentChildId,
                commentChildReportRegisterAPIRequest.toReportRegisterAPIRequest(userId));
        return ApiResponse.of(
                ReportRegisterApiResponse.builder()
                        .reportId(reportId)
                        .build());
    }
}
