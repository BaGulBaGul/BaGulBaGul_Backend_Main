package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.api.response;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.GetLikeRecruitmentResponse;
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
public class GetLikeRecruitmentApiResponse {

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

    public static GetLikeRecruitmentApiResponse from(GetLikeRecruitmentResponse getLikeRecruitmentResponse) {
        return GetLikeRecruitmentApiResponse.builder()
                .recruitmentId(getLikeRecruitmentResponse.getRecruitmentId())
                .startDate(getLikeRecruitmentResponse.getStartDate())
                .endDate(getLikeRecruitmentResponse.getEndDate())
                .title(getLikeRecruitmentResponse.getTitle())
                .eventId(getLikeRecruitmentResponse.getEventId())
                .eventTitle(getLikeRecruitmentResponse.getEventTitle())
                .build();
    }
}
