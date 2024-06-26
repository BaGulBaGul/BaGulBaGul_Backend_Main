package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RecruitmentSimpleInfo {
    @ApiModelProperty(value = "모잡글 id")
    private Long recruitmentId;

    @ApiModelProperty(value = "모집 상태")
    private RecruitmentState state;

    @ApiModelProperty(value = "참여 인원")
    private Integer currentHeadCount;

    @ApiModelProperty(value = "모집 인원")
    private Integer maxHeadCount;

    @ApiModelProperty(value = "시작 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
}
