package com.BaGulBaGul.BaGulBaGul.domain.user.info.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserModifyRequest {
    @ApiModelProperty(value = "이메일")
    String email;
    @ApiModelProperty(value = "프로필 상태 메세지")
    String profileMessage;
    @ApiModelProperty(value = "프로필 이미지의 resource id")
    Long imageResourceId;
}
