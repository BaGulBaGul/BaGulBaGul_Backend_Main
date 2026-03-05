package com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportContentType;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportStatusState;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportStatusInfo;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
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
public class ReportStatusApiResponse {
    @ApiModelProperty(value = "ReportStatus의 id")
    private Long reportStatusId;
    @ApiModelProperty(value = "ReportStatus 처리 상태. PROCEEDING: 진행중, ACCEPTED: 승인됨. CANCELED: 취소됨")
    private ReportStatusState state;
    @ApiModelProperty(value = "게시물 타입. 이벤트, 모집글, 댓글, 대댓글 중 하나")
    private ReportContentType reportContentType;

    @ApiModelProperty(value = "전체 신고 수")
    private int totalReportCount;
    @ApiModelProperty(value = "신고 내용이 관련없는 게시물인 신고 수")
    private int notRelevantReportCount;
    @ApiModelProperty(value = "신고 내용이 공격적인 게시물인 신고 수")
    private int offensiveContentReportCount;
    @ApiModelProperty(value = "신고 내용이 명예훼손성 게시물인 신고 수")
    private int defamatoryReportCount;
    @ApiModelProperty(value = "신고 내용이 기타인 신고 수")
    private int ectReportCount;

    @ApiModelProperty(value = "신고 처리 결과 대상 게시물을 삭제했는지")
    private boolean reportedContentDeleted;
    @ApiModelProperty(value = "신고 처리 결과 대상 게시물 작성자를 정지했는지")
    private boolean reportedContentWriterSuspended;

    public static ReportStatusApiResponse from(ReportStatusInfo reportStatusInfo) {
        return ReportStatusApiResponse.builder()
                .reportStatusId(reportStatusInfo.getReportStatusId())
                .state(reportStatusInfo.getState())
                .reportContentType(reportStatusInfo.getReportContentType())
                .totalReportCount(reportStatusInfo.getTotalReportCount())
                .notRelevantReportCount(reportStatusInfo.getNotRelevantReportCount())
                .offensiveContentReportCount(reportStatusInfo.getOffensiveContentReportCount())
                .defamatoryReportCount(reportStatusInfo.getDefamatoryReportCount())
                .ectReportCount(reportStatusInfo.getEctReportCount())
                .reportedContentDeleted(reportStatusInfo.isReportedContentDeleted())
                .reportedContentWriterSuspended(reportStatusInfo.isReportedContentWriterSuspended())
                .build();
    }
}
