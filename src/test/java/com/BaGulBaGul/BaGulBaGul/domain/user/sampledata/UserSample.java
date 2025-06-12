package com.BaGulBaGul.BaGulBaGul.domain.user.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserRegisterRequest;
import org.openapitools.jackson.nullable.JsonNullable;

public abstract class UserSample {
    public static final String NORMAL_EMAIL = "osm@naver.com";
    public static final String NORMAL_USERNAME = "osm입니다";
    public static final String NORMAL_USERNAME_UPPERCASE = "OSM입니다";
    public static final String NORMAL_PROFILE_MESSAGE = "안녕하세요";

    public static final String NORMAL_EMAIL2 = "osm@naver.com";
    public static final String NORMAL_USERNAME2 = "osmosm입니다";
    public static final String NORMAL_PROFILE_MESSAGE2 = "안녕하세요2";

    public static final String UNNORMAL_EMAIL = "osm@";
    public static final String UNNORMAL_USERNAME = "o";


    //DB
    public static final String CONSTRAINT_UNIQUE_USERNAME = "UK__USER__NICKNAME";

    public static UserRegisterRequest getNormalUserRegisterRequest() {
        return UserRegisterRequest.builder()
                .nickname(NORMAL_USERNAME)
                .email(NORMAL_EMAIL)
                .build();
    }

    public static UserModifyRequest getNormalUserModifyRequest() {
        return UserModifyRequest.builder()
                .username(JsonNullable.of(NORMAL_USERNAME2))
                .profileMessage(JsonNullable.of(NORMAL_PROFILE_MESSAGE2))
                .email(JsonNullable.of(NORMAL_EMAIL2))
                .build();
    }

    public static UserRegisterRequest getUnNormalUserRegisterRequest() {
        return UserRegisterRequest.builder()
                .nickname(UNNORMAL_USERNAME)
                .email(UNNORMAL_EMAIL)
                .build();
    }
}
