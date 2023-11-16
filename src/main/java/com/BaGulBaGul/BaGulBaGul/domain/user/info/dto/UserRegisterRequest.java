package com.BaGulBaGul.BaGulBaGul.domain.user.info.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserRegisterRequest {
    String nickname;
    String email;
}
