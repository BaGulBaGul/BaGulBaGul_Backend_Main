package com.BaGulBaGul.BaGulBaGul.domain.report.service;

import com.BaGulBaGul.BaGulBaGul.domain.report.ReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request.CompleteReportStatusRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.request.FindReportStatusByConditionRequest;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.FindReportStatusByConditionResponse;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportStatusService {
    Page<FindReportStatusByConditionResponse> findReportStatusByCondition(
            FindReportStatusByConditionRequest conditionRequest, Pageable pageable);
    List<FindReportStatusByConditionResponse> getConditionResponseWithFetch(List<ReportStatus> reportStatuses);
    void completeReportStatus(
            AuthenticatedUserInfo authenticatedUserInfo,
            Long reportStatusId,
            CompleteReportStatusRequest completeReportStatusRequest
    );
}
