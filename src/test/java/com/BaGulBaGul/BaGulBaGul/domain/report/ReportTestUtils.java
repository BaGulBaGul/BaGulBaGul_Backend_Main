package com.BaGulBaGul.BaGulBaGul.domain.report;

import com.BaGulBaGul.BaGulBaGul.domain.event.EventTestUtils;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostTestUtils;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.RecruitmentTestUtils;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportContentType;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportStatusState;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.FindReportStatusByConditionResponse;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportInfo;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportStatusInfo;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportStatusOriginalContentInfo;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class ReportTestUtils {
    public static void assertReportStatus(
            ReportStatus reportStatus, ReportStatusState reportStatusState,
            int totalReportCount, int notRelevantReportCount, int offensiveContentReportCount,
            int defamatoryReportCount, int ectReportCount,
            boolean isReportedContentDeleted, boolean isReportedContentWriterSuspended
    ) {
        assertThat(reportStatus.getState()).isEqualTo(reportStatusState);
        assertThat(reportStatus.getTotalReportCount()).isEqualTo(totalReportCount);
        assertThat(reportStatus.getNotRelevantReportCount()).isEqualTo(notRelevantReportCount);
        assertThat(reportStatus.getOffensiveContentReportCount()).isEqualTo(offensiveContentReportCount);
        assertThat(reportStatus.getDefamatoryReportCount()).isEqualTo(defamatoryReportCount);
        assertThat(reportStatus.getEctReportCount()).isEqualTo(ectReportCount);
        assertThat(reportStatus.isReportedContentDeleted()).isEqualTo(isReportedContentDeleted);
        assertThat(reportStatus.isReportedContentWriterSuspended()).isEqualTo(isReportedContentWriterSuspended);
    }

    public static void assertReportStatusInfo(ReportStatusInfo reportStatusInfo, ReportStatus reportStatus) {
        assertThat(reportStatusInfo.getReportStatusId()).isEqualTo(reportStatus.getId());
        assertThat(reportStatusInfo.getState()).isEqualTo(reportStatus.getState());
        if (reportStatus instanceof EventReportStatus) {
            assertThat(reportStatusInfo.getReportContentType()).isEqualTo(ReportContentType.Event);
        } else if (reportStatus instanceof RecruitmentReportStatus) {
            assertThat(reportStatusInfo.getReportContentType()).isEqualTo(ReportContentType.Recruitment);
        } else if (reportStatus instanceof CommentReportStatus) {
            assertThat(reportStatusInfo.getReportContentType()).isEqualTo(ReportContentType.Comment);
        } else if (reportStatus instanceof CommentChildReportStatus) {
            assertThat(reportStatusInfo.getReportContentType()).isEqualTo(ReportContentType.CommentChild);
        }
        assertThat(reportStatusInfo.getTotalReportCount()).isEqualTo(reportStatus.getTotalReportCount());
        assertThat(reportStatusInfo.getNotRelevantReportCount()).isEqualTo(reportStatus.getNotRelevantReportCount());
        assertThat(reportStatusInfo.getOffensiveContentReportCount()).isEqualTo(reportStatus.getOffensiveContentReportCount());
        assertThat(reportStatusInfo.getDefamatoryReportCount()).isEqualTo(reportStatus.getDefamatoryReportCount());
        assertThat(reportStatusInfo.getEctReportCount()).isEqualTo(reportStatus.getEctReportCount());
        assertThat(reportStatusInfo.isReportedContentDeleted()).isEqualTo(reportStatus.isReportedContentDeleted());
        assertThat(reportStatusInfo.isReportedContentWriterSuspended()).isEqualTo(reportStatus.isReportedContentWriterSuspended());
    }

    public static void assertFindReportStatusByConditionResponse(FindReportStatusByConditionResponse response, ReportStatus reportStatus) {
        assertReportStatusInfo(response.getReportStatusInfo(), reportStatus);
        ReportStatusOriginalContentInfo contentInfo = response.getReportStatusOriginalContentInfo();
        if (reportStatus instanceof EventReportStatus) {
            EventReportStatus eventReportStatus = (EventReportStatus) reportStatus;
            assertThat(contentInfo.getEventInfo()).isPresent();
            EventTestUtils.assertEventSimpleResponse(contentInfo.getEventInfo().get(), eventReportStatus.getEvent());
        } else if (reportStatus instanceof RecruitmentReportStatus) {
            RecruitmentReportStatus recruitmentReportStatus = (RecruitmentReportStatus) reportStatus;
            assertThat(contentInfo.getRecruitmentInfo()).isPresent();
            RecruitmentTestUtils.assertRecruitmentSimpleResponse(contentInfo.getRecruitmentInfo().get(), recruitmentReportStatus.getRecruitment());
        } else if (reportStatus instanceof CommentReportStatus) {
            CommentReportStatus commentReportStatus = (CommentReportStatus) reportStatus;
            assertThat(contentInfo.getCommentInfo()).isPresent();
            PostTestUtils.assertPostCommentInfo(contentInfo.getCommentInfo().get(), commentReportStatus.getPostComment());
        } else if (reportStatus instanceof CommentChildReportStatus) {
            CommentChildReportStatus commentChildReportStatus = (CommentChildReportStatus) reportStatus;
            assertThat(contentInfo.getCommentChildInfo()).isPresent();
            PostTestUtils.assertPostCommentChildInfo(contentInfo.getCommentChildInfo().get(), commentChildReportStatus.getPostCommentChild());
        }
    }

    public static void assertReportInfo(ReportInfo reportInfo, Report report) {
        assertThat(reportInfo.getReportId()).isEqualTo(report.getReportId());
        assertThat(reportInfo.getReportType()).isEqualTo(report.getReportType());
        assertThat(reportInfo.isSolved()).isEqualTo(report.isSolved());
        assertThat(reportInfo.getMessage()).isEqualTo(report.getMessage());
        UserTestUtils.assertUserInfoResponse(reportInfo.getReportingUserInfo(), report.getReportingUser());
        UserTestUtils.assertUserInfoResponse(reportInfo.getReportedUserInfo(), report.getReportedUser());
        assertThat(reportInfo.getReportedAt()).isEqualTo(report.getCreatedAt());
    }
}
