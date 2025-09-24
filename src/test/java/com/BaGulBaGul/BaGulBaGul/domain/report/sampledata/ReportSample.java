package com.BaGulBaGul.BaGulBaGul.domain.report.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportType;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.ReportRegisterRequest;

public class ReportSample {
    public static final ReportType NORMAL_REPORT_TYPE = ReportType.OFFENSIVE_CONTENT;
    public static final String NORMAL_REPORT_MESSAGE = "test";
    public static final ReportType NORMAL2_REPORT_TYPE = ReportType.NOT_RELEVANT;
    public static final String NORMAL2_REPORT_MESSAGE = "test2";
    public static final ReportType NORMAL3_REPORT_TYPE = ReportType.DEFAMATORY;
    public static final String NORMAL3_REPORT_MESSAGE = "test3";

    public static ReportRegisterRequest getNormalReportRegisterRequest(Long reportingUserId) {
        return ReportRegisterRequest.builder()
                .reportingUserId(reportingUserId)
                .reportType(NORMAL_REPORT_TYPE)
                .message(NORMAL_REPORT_MESSAGE)
                .build();
    }
    public static ReportRegisterRequest getNormal2ReportRegisterRequest(Long reportingUserId) {
        return ReportRegisterRequest.builder()
                .reportingUserId(reportingUserId)
                .reportType(NORMAL2_REPORT_TYPE)
                .message(NORMAL2_REPORT_MESSAGE)
                .build();
    }
    public static ReportRegisterRequest getNormal3ReportRegisterRequest(Long reportingUserId) {
        return ReportRegisterRequest.builder()
                .reportingUserId(reportingUserId)
                .reportType(NORMAL3_REPORT_TYPE)
                .message(NORMAL3_REPORT_MESSAGE)
                .build();
    }
}
