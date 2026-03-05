package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
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
public class RecruitmentDetailInfo {

    private Long recruitmentId;

    private Long eventId;

    private RecruitmentState state;

    private Integer currentHeadCount;

    private Integer maxHeadCount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

}
