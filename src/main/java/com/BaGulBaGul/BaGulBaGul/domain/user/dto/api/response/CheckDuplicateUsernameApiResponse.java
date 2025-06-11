package com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckDuplicateUsernameApiResponse {
    @ApiModelProperty(value = "닉네임 중복 여부")
    boolean duplicate;
}
