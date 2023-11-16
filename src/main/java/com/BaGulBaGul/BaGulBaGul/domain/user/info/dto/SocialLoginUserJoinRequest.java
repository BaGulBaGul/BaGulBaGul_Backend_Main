package com.BaGulBaGul.BaGulBaGul.domain.user.info.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class SocialLoginUserJoinRequest {

    @ApiModelProperty(value = "oauth2 인증 후 프론트에 url 파라메터로 넘겨준 joinToken을 그대로 전달 | 필수")
    @NotBlank
    String joinToken;

    @ApiModelProperty(value = "유저 닉네임 | 필수")
    @NotBlank
    String nickname;

    @ApiModelProperty(value = "이메일 정보. 선택사항")
    String email;

    public UserRegisterRequest toUserRegisterRequest() {
        return UserRegisterRequest.builder()
                .email(email)
                .nickname(nickname)
                .build();
    }
}
