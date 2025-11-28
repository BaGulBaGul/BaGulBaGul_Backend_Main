package com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManagePasswordLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.PasswordLoginUserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserRegisterRequest;
import io.swagger.annotations.ApiModelProperty;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AMPWUserRegisterApiRequest {
    @ApiModelProperty(value = "닉네임", required = true)
    private String nickname;

    @ApiModelProperty(value = "이메일")
    private String email;

    @ApiModelProperty(value = "로그인 id")
    private String loginId;

    @ApiModelProperty(value = "로그인 pw")
    private String loginPw;


    public AdminManagePasswordLoginUserJoinRequest toServiceRequest() {
        return new AdminManagePasswordLoginUserJoinRequest(
                UserRegisterRequest.builder()
                        .nickname(nickname)
                        .email(email)
                        .roles(Arrays.asList("EVENT_HOST"))
                        .build(),
                PasswordLoginUserRegisterRequest.builder()
                        .loginId(loginId)
                        .loginPassword(loginPw)
                        .build()
        );
    }
}
