package com.BaGulBaGul.BaGulBaGul.domain.report.service;

import com.BaGulBaGul.BaGulBaGul.domain.report.dto.ReportRegisterRequest;

public interface ReportService {
    Long registerEventReport(Long eventId, ReportRegisterRequest reportRegisterRequest);
    Long registerRecruitmentReport(Long recruitmentId, ReportRegisterRequest reportRegisterRequest);
    Long registerPostCommentReport(Long postCommentId, ReportRegisterRequest reportRegisterRequest);
    Long registerPostCommentChildReport(Long postCommentChildId, ReportRegisterRequest reportRegisterRequest);
}
