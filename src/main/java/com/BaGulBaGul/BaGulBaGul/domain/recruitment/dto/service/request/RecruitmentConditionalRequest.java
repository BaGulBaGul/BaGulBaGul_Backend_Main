package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostConditionalRequest;
import java.util.List;
import javax.validation.Valid;
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

    private Long eventId;

    private Integer leftHeadCount;

    @Valid
    @Builder.Default
    private PostConditionalRequest postConditionalRequest = new PostConditionalRequest();

}
