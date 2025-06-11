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
    @NotBlank
    String joinToken;

    @ApiModelProperty(value = "유저 닉네임 | 필수")
    @Pattern(regexp = "^[가-힣a-zA-Z]+$", message = "유저명은 한글이나 영어로만 설정할 수 있습니다.")
    @Size(min = 2, max = 12, message = "유저명은 {2}이상 {1}이하의 글자여야 합니다.")
    @NotNull(message = "유저명은 필수 입력사항입니다.")
    String nickname;

    @ApiModelProperty(value = "이메일 정보. 선택사항")
    @Email(message = "이메일 형식이 아닙니다.")
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
