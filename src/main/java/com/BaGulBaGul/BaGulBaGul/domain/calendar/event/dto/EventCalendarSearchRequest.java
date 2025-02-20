package com.BaGulBaGul.BaGulBaGul.domain.calendar.event.dto;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventCalendarSearchRequest {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @ApiModelProperty(value = "검색 시작 시간")
    private LocalDateTime searchStartTime;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @ApiModelProperty(value = "검색 종료 시간")
    private LocalDateTime searchEndTime;
}
