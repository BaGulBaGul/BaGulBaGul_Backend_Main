package com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportType;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportInfo;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response.UserInfoApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserInfoResponse;
import io.swagger.annotations.ApiModelProperty;
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
public class ReportApiResponse {
    @ApiModelProperty("report id")
    private Long reportId;
    @ApiModelProperty("report 타입(NOT_RELEVANT, OFFENSIVE_CONTENT, DEFAMATORY, ETC)")
    private ReportType reportType;
    @ApiModelProperty("처리 여부")
    private boolean solved;
    @ApiModelProperty("메세지")
    private String message;
    @ApiModelProperty("신고자 유저 정보")
    private UserInfoApiResponse reportingUserInfo;
    @ApiModelProperty("신고 대상 유저 정보")
    private UserInfoApiResponse reportedUserInfo;
    @ApiModelProperty("신고 일시")
    private LocalDateTime reportedAt;
    @ApiModelProperty("이벤트 id(이벤트에 대한 신고가 아니라면 null)")
    private Long eventId;
    @ApiModelProperty("모집글 id(모집글에 대한 신고가 아니라면 null)")
    private Long recruitmentId;
    @ApiModelProperty("댓글 id(댓글에 대한 신고가 아니라면 null)")
    private Long commentId;
    @ApiModelProperty("대댓글 id(대댓글에 대한 신고가 아니라면 null)")
    private Long commentChildId;

    public static ReportApiResponse from(ReportInfo reportInfo) {
        return ReportApiResponse.builder()
                .reportId(reportInfo.getReportId())
                .reportType(reportInfo.getReportType())
                .solved(reportInfo.isSolved())
                .message(reportInfo.getMessage())
                .reportingUserInfo(UserInfoApiResponse.from(reportInfo.getReportingUserInfo()))
                .reportedUserInfo(UserInfoApiResponse.from(reportInfo.getReportedUserInfo()))
                .reportedAt(reportInfo.getReportedAt())
                .eventId(reportInfo.getEventId().orElse(null))
                .recruitmentId(reportInfo.getRecruitmentId().orElse(null))
                .commentId(reportInfo.getCommentId().orElse(null))
                .commentChildId(reportInfo.getCommentChildId().orElse(null))
                .build();
    }
}
