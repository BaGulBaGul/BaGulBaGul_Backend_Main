package com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.LocationModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostModifyRequest;
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
public class EventModifyRequest {

    private EventType type;

    @Builder.Default
    private JsonNullable<Long> eventHostUserId = JsonNullable.undefined();

    private Boolean ageLimit;

    @Size(max = 2, message = "카테고리 개수는 {1}개 이하여야 합니다.")
    private List<String> categories;

    @Valid
    @Builder.Default
    private LocationModifyRequest locationModifyRequest = new LocationModifyRequest();

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
