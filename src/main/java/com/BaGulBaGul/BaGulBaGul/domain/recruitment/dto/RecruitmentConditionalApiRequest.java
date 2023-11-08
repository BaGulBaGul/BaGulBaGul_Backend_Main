package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class RecruitmentConditionalApiRequest {

    private String title;
    private List<String> tags;
    private String username;

    public RecruitmentConditionalRequest toRecruitmentConditionalRequest(Long eventId) {
        return RecruitmentConditionalRequest.builder()
                .eventId(eventId)
                .title(title)
                .tags(tags)
                .username(username)
                .build();
    }
}
