package com.BaGulBaGul.BaGulBaGul.domain.user.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.constant.OAuth2Provider;

public class SocialLoginUserSample {
    public static final String MOCK_JOIN_TOKEN = "xxx";
    public static final OAuth2Provider NORMAL_OAUTH2_PROVIDER = OAuth2Provider.kakao;
    public static final String NORMAL_OAUTH2_ID = "testtest";

    public static SocialLoginUserJoinRequest getMockSocialLoginUserJoinRequest() {
        return SocialLoginUserJoinRequest.builder()
                .joinToken(MOCK_JOIN_TOKEN)
                .userRegisterRequest(UserSample.getNormalUserRegisterRequest())
                .build();
    }
}

