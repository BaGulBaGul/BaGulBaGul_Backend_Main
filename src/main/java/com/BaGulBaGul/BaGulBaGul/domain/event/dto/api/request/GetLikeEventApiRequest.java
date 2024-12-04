package com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.GetLikeEventRequest;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
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
public class GetLikeEventApiRequest {
    @ApiModelProperty(value = "이벤트 타입 FESTIVAL, LOCAL_EVENT, PARTY 중 하나 | 필수")
    @NotNull(message="이벤트 타입은 필수 값입니다.")
    EventType type;

    public GetLikeEventRequest toGetLikeEventRequest() {
        throw new UnsupportedOperationException();
    }
}
