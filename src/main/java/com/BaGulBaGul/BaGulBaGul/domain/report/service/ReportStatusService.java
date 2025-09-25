package com.BaGulBaGul.BaGulBaGul.domain.report.service;

import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request.CompleteReportStatusRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;

public interface ReportStatusService {
    void completeReportStatus(
            AuthenticatedUserInfo authenticatedUserInfo,
            Long reportStatusId,
            CompleteReportStatusRequest completeReportStatusRequest
    );
}
