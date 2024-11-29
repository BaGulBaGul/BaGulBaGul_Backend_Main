package com.BaGulBaGul.BaGulBaGul.domain.user.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserRegisterRequest;

public abstract class UserSample {
    public static final String NORMAL_EMAIL = "osm@naver.com";
    public static final String NORMAL_USERNAME = "osm입니다";
    public static final String NORMAL_USERNAME_UPPERCASE = "OSM입니다";

    public static final UserRegisterRequest NORMAL_USER_REGISTER_REQUEST = UserRegisterRequest.builder()
            .nickname(NORMAL_USERNAME)
            .email(NORMAL_EMAIL)
            .build();


    public static final String CONSTRAINT_UNIQUE_USERNAME = "UK__USER__NICKNAME";
}
