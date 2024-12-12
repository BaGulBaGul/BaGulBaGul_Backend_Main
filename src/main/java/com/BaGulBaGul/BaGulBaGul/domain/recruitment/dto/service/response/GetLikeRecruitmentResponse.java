package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
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
public class GetLikeRecruitmentResponse {

    @ApiModelProperty(value = "모집글 id")
    private Long recruitmentId;

    @ApiModelProperty(value = "시작 시간")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "종료 시간")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "게시글 제목")
    private String title;

    @ApiModelProperty(value = "이벤트 id")
    private Long eventId;

    @ApiModelProperty(value = "이벤트 제목")
    private String eventTitle;

    public static GetLikeRecruitmentResponse of(Recruitment recruitment) {
        return GetLikeRecruitmentResponse.builder()
                .recruitmentId(recruitment.getId())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .title(recruitment.getPost().getTitle())
                .eventId(recruitment.getEvent().getId())
                .eventTitle(recruitment.getEvent().getPost().getTitle())
                .build();
    }
}
