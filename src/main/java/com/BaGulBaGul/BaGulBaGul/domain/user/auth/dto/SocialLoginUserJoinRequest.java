package com.BaGulBaGul.BaGulBaGul.domain.user.auth.dto;

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
    @NotBlank
    String joinToken;
    @NotBlank
    String nickname;
    String email;

    public UserRegisterRequest toUserRegisterRequest() {
        return UserRegisterRequest.builder()
                .email(email)
                .nickname(nickname)
                .build();
    }
}
