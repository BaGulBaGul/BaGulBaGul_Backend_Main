package com.BaGulBaGul.BaGulBaGul.domain.user.info.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserModifyRequest {
    @ApiModelProperty(value = "이메일")
    JsonNullable<String> email = JsonNullable.undefined();
    @ApiModelProperty(value = "프로필 상태 메세지")
    JsonNullable<String> profileMessage = JsonNullable.undefined();
    @ApiModelProperty(value = "프로필 이미지의 resource id")
    JsonNullable<Long> imageResourceId = JsonNullable.undefined();
}
