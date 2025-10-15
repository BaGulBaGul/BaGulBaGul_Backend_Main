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

    private Long recruitmentId;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String title;

    private Long eventId;

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
