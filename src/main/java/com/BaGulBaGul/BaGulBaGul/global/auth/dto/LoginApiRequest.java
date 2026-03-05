package com.BaGulBaGul.BaGulBaGul.global.auth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginApiRequest {
    @ApiModelProperty(value = "로그인 아이디")
    private String loginId;
    @ApiModelProperty(value = "로그인 비밀번호")
    private String loginPw;
}
