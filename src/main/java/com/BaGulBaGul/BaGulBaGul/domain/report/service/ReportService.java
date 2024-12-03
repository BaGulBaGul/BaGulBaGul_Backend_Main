package com.BaGulBaGul.BaGulBaGul.domain.report.service;

import com.BaGulBaGul.BaGulBaGul.domain.report.dto.ReportRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.exception.DuplicateReportException;

public interface ReportService {
    Long registerEventReport(Long eventId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException;
    Long registerRecruitmentReport(Long recruitmentId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException;
    Long registerPostCommentReport(Long postCommentId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException;
    Long registerPostCommentChildReport(Long postCommentChildId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException;
}
