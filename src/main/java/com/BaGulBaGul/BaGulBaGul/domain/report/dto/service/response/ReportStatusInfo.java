package com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.report.CommentChildReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.EventReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.RecruitmentReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.ReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportContentType;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportStatusState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportStatusInfo {
    private Long reportStatusId;

    private ReportStatusState state;
    private ReportContentType reportContentType;
    private int totalReportCount;
    private int notRelevantReportCount;
    private int offensiveContentReportCount;
    private int defamatoryReportCount;
    private int ectReportCount;

    private boolean reportedContentDeleted;
    private boolean reportedContentWriterSuspended;

    public static ReportStatusInfo from(ReportStatus reportStatus) {
        ReportStatusInfo result = ReportStatusInfo.builder()
                .reportStatusId(reportStatus.getId())
                .state(reportStatus.getState())
                .totalReportCount(reportStatus.getTotalReportCount())
                .notRelevantReportCount(reportStatus.getNotRelevantReportCount())
                .offensiveContentReportCount(reportStatus.getOffensiveContentReportCount())
                .defamatoryReportCount(reportStatus.getDefamatoryReportCount())
                .ectReportCount(reportStatus.getEctReportCount())
                .reportedContentDeleted(reportStatus.isReportedContentDeleted())
                .reportedContentWriterSuspended(reportStatus.isReportedContentWriterSuspended())
                .build();
        if(reportStatus instanceof EventReportStatus) {
            result.setReportContentType(ReportContentType.Event);
        } else if(reportStatus instanceof RecruitmentReportStatus) {
            result.setReportContentType(ReportContentType.Recruitment);
        } else if(reportStatus instanceof CommentReportStatus) {
            result.setReportContentType(ReportContentType.Comment);
        } else if(reportStatus instanceof CommentChildReportStatus) {
            result.setReportContentType(ReportContentType.CommentChild);
        }
        return result;
    }
}
