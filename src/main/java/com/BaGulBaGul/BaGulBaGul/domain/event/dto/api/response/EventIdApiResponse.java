package com.BaGulBaGul.BaGulBaGul.domain.event.dto.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventIdApiResponse {
    @ApiModelProperty(value = "이벤트 id")
    private Long eventId;
}
