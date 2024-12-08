package com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruitmentModifyRequest {

    @ApiModelProperty(value = "모집 상태")
    private RecruitmentState state;

    @Valid
    @Builder.Default
    private ParticipantStatusModifyRequest participantStatusModifyRequest = new ParticipantStatusModifyRequest();

    @Valid
    @Builder.Default
    private PeriodModifyRequest periodModifyRequest = new PeriodModifyRequest();

    @Valid
    @Builder.Default
    private PostModifyRequest postModifyRequest = new PostModifyRequest();

}
