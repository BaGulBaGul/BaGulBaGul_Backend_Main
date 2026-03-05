package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentDetailInfo;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RecruitmentDetailApiInfo {

    @ApiModelProperty(value = "모잡글 id")
    private Long recruitmentId;

    @ApiModelProperty(value = "이벤트 id")
    private Long eventId;

    @ApiModelProperty(value = "모집 상태")
    private RecruitmentState state;

    @ApiModelProperty(value = "참여 인원")
    private Integer currentHeadCount;

    @ApiModelProperty(value = "모집 인원")
    private Integer maxHeadCount;

    @ApiModelProperty(value = "시작 시간")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    private LocalDateTime endDate;

    public static RecruitmentDetailApiInfo from(RecruitmentDetailInfo recruitmentDetailInfo) {
        return RecruitmentDetailApiInfo.builder()
                .recruitmentId(recruitmentDetailInfo.getRecruitmentId())
                .eventId(recruitmentDetailInfo.getEventId())
                .state(recruitmentDetailInfo.getState())
                .currentHeadCount(recruitmentDetailInfo.getCurrentHeadCount())
                .maxHeadCount(recruitmentDetailInfo.getMaxHeadCount())
                .startDate(recruitmentDetailInfo.getStartDate())
                .endDate(recruitmentDetailInfo.getEndDate())
                .build();
    }
}
