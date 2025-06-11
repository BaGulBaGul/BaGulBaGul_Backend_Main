package com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class SocialLoginUserJoinRequest {
    String joinToken;

    UserRegisterRequest userRegisterRequest;
}
