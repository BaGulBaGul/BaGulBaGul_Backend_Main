package com.BaGulBaGul.BaGulBaGul.domain.event.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EventRegisterResponse {
    @ApiModelProperty(value = "이벤트 id")
    private Long id;
}
