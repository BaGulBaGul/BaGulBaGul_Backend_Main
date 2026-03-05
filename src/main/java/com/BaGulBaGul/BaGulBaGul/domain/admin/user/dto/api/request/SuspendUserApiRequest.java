package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuspendUserApiRequest {
    @NotNull
    @ApiModelProperty(value = "정지 사유")
    private String reason;

    @NotNull
    @Future
    @ApiModelProperty(value = "정지 종료 시간")
    private LocalDateTime endDate;
}
