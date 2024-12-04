package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostConditionalRequest;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentConditionalRequest {

    private String title;

    private List<String> tags;

    private String username;

    private Integer leftHeadCount;

    private Long eventId;

    public PostConditionalRequest toPostConditionalRequest() {
        return PostConditionalRequest.builder()
                .title(title)
                .username(username)
                .tags(tags)
                .build();
    }
}
