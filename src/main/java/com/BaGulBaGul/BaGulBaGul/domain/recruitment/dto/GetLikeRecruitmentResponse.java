package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
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
    private Long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String title;

    public static GetLikeRecruitmentResponse of(Recruitment recruitment) {
        return GetLikeRecruitmentResponse.builder()
                .id(recruitment.getId())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .title(recruitment.getPost().getTitle())
                .build();
    }
}
