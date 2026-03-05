package com.BaGulBaGul.BaGulBaGul.domain.report.service;

import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request.ReportRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportInfo;
import com.BaGulBaGul.BaGulBaGul.domain.report.exception.DuplicateReportException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {
    Page<ReportInfo> findByReportStatusIdAndPageable(Long reportStatusId, Pageable pageable);
    Long registerEventReport(Long eventId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException;
    Long registerRecruitmentReport(Long recruitmentId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException;
    Long registerPostCommentReport(Long postCommentId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException;
    Long registerPostCommentChildReport(Long postCommentChildId, ReportRegisterRequest reportRegisterRequest) throws DuplicateReportException;
}
