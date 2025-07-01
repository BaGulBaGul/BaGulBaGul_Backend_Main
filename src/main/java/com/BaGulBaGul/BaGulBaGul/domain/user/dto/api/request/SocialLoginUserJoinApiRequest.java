package com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserRegisterRequest;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginUserJoinApiRequest {

    @ApiModelProperty(value = "oauth2 인증 후 프론트에 url 파라메터로 넘겨준 joinToken을 그대로 전달 | 필수")
    String joinToken;

    @ApiModelProperty(value = "유저 닉네임 | 필수")
    String nickname;

    @ApiModelProperty(value = "이메일 정보. 선택사항")
    String email;


    public SocialLoginUserJoinRequest toSocialLoginUserJoinRequest() {
        return SocialLoginUserJoinRequest.builder()
                .joinToken(joinToken)
                .userRegisterRequest(UserRegisterRequest.builder()
                        .nickname(nickname)
                        .email(email)
                        .build())
                .build();
    }
}
