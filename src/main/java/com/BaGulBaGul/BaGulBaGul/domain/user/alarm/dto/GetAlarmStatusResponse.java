package com.BaGulBaGul.BaGulBaGul.domain.user.alarm.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetAlarmStatusResponse {
    @ApiModelProperty(value = "총 알람 개수")
    private Long totalAlarmCount;
    @ApiModelProperty(value = "체크하지 않은 알람 개수")
    private Long uncheckedAlarmCount;
}
