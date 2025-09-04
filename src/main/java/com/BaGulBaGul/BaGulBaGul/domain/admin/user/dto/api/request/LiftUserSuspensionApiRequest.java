package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LiftUserSuspensionApiRequest {
    @NotNull
    @ApiModelProperty(value = "정지 사유")
    private String reason;
}
