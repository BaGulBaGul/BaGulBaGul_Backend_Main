package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RecruitmentSimpleResponse {
    private Long id;
    private RecruitmentState state;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;
    private String title;
    private String username;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static RecruitmentSimpleResponse of(Recruitment recruitment) {
        return RecruitmentSimpleResponse.builder()
                .id(recruitment.getId())
                .startDate(recruitment.getStartDate())
                .endDate(recruitment.getEndDate())
                .state(recruitment.getState())
                .title(recruitment.getPost().getTitle())
                .username(recruitment.getPost().getUser().getNickname())
                .tags(Arrays.asList(recruitment.getPost().getTags().split(" ")))
                .createdAt(recruitment.getPost().getCreatedAt())
                .lastModifiedAt(recruitment.getPost().getLastModifiedAt())
                .build();
    }
}
