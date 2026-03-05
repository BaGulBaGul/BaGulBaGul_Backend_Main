package com.BaGulBaGul.BaGulBaGul.domain.admin.report.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response.EventPageApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostCommentChildApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostCommentDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response.RecruitmentPageApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.api.response.ReportStatusApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.FindReportStatusByConditionResponse;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportStatusInfo;
import com.BaGulBaGul.BaGulBaGul.domain.report.dto.service.response.ReportStatusOriginalContentInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
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
@ApiModel(description = "ReportStatus(게시물에 대한 신고 상태) 조건 페이지 검색 결과")
public class FindReportStatusByConditionApiResponse {

    @ApiModelProperty(value = "ReportStatus 정보")
    private ReportStatusApiResponse reportStatusInfo;

    @ApiModelProperty(value = "이벤트 정보. 없다면 null")
    private EventPageApiResponse eventInfo;
    @ApiModelProperty(value = "모집글 정보. 없다면 null")
    private RecruitmentPageApiResponse recruitmentInfo;
    @ApiModelProperty(value = "댓글 정보. 없다면 null")
    private PostCommentDetailResponse commentInfo;
    @ApiModelProperty(value = "대댓글 정보. 없다면 null")
    private PostCommentChildApiResponse commentChildInfo;

    public static FindReportStatusByConditionApiResponse from(FindReportStatusByConditionResponse response) {
        ReportStatusInfo reportStatusInfo = response.getReportStatusInfo();
        ReportStatusOriginalContentInfo originalContentInfo = response.getReportStatusOriginalContentInfo();
        FindReportStatusByConditionApiResponse result = FindReportStatusByConditionApiResponse.builder()
                .reportStatusInfo(ReportStatusApiResponse.from(reportStatusInfo))
                .eventInfo(originalContentInfo.getEventInfo()
                        .map(EventPageApiResponse::from).orElse(null))
                .recruitmentInfo(originalContentInfo.getRecruitmentInfo()
                        .map(RecruitmentPageApiResponse::from).orElse(null))
                .commentInfo(originalContentInfo.getCommentInfo()
                        .map(PostCommentDetailResponse::from).orElse(null))
                .commentChildInfo(originalContentInfo.getCommentChildInfo()
                        .map(PostCommentChildApiResponse::from).orElse(null))
                .build();
        return result;
    }


}
