package com.BaGulBaGul.BaGulBaGul.domain.user.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.SocialLoginUserJoinRequest;

public class SocialLoginUserSample {
    public static final String NORMAL_JOIN_TOKEN = "xxx";

    public static SocialLoginUserJoinRequest getNormalSocialLoginUserJoinRequest() {
        return SocialLoginUserJoinRequest.builder()
                .joinToken(NORMAL_JOIN_TOKEN)
                .userRegisterRequest(UserSample.getNormalUserRegisterRequest())
                .build();
    }
}

