package com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.report.CommentChildReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.EventReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.RecruitmentReport;
import com.BaGulBaGul.BaGulBaGul.domain.report.Report;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportType;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserInfoResponse;
import java.time.LocalDateTime;
import java.util.Optional;
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
public class ReportInfo {
    private Long reportId;
    private ReportType reportType;
    private boolean solved;
    private String message;
    private UserInfoResponse reportingUserInfo;
    private UserInfoResponse reportedUserInfo;
    private LocalDateTime reportedAt;
    @Builder.Default
    private Optional<Long> eventId = Optional.empty();
    @Builder.Default
    private Optional<Long> recruitmentId = Optional.empty();
    @Builder.Default
    private Optional<Long> commentId = Optional.empty();
    @Builder.Default
    private Optional<Long> commentChildId = Optional.empty();

    public static ReportInfo from(Report report) {
        ReportInfo reportInfo = ReportInfo.builder()
                .reportId(report.getReportId())
                .reportType(report.getReportType())
                .solved(report.isSolved())
                .message(report.getMessage())
                .reportedUserInfo(UserInfoResponse.from(report.getReportedUser()))
                .reportingUserInfo(UserInfoResponse.from(report.getReportingUser()))
                .reportedAt(report.getCreatedAt())
                .build();
        if(report instanceof EventReport) {
            reportInfo.setEventId(Optional.of(((EventReport) report).getEvent().getId()));
        } else if(report instanceof RecruitmentReport) {
            reportInfo.setRecruitmentId(Optional.of(((RecruitmentReport) report).getRecruitment().getId()));
        } else if(report instanceof CommentReport) {
            reportInfo.setCommentId(Optional.of(((CommentReport) report).getPostComment().getId()));
        } else if(report instanceof CommentChildReport) {
            reportInfo.setCommentChildId(Optional.of(((CommentChildReport) report).getPostCommentChild().getId()));
        }
        return reportInfo;
    }
}
