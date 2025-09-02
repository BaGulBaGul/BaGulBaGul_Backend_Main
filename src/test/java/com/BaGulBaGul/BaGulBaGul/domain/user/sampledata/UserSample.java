package com.BaGulBaGul.BaGulBaGul.domain.user.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;

public abstract class UserSample {
    public static final String NORMAL_EMAIL = "osm@naver.com";
    public static final String NORMAL_USERNAME = "osm입니다";
    public static final String NORMAL_PROFILE_MESSAGE = "안녕하세요";

    public static final String NORMAL_EMAIL2 = "osm2@naver.com";
    public static final String NORMAL_USERNAME2 = "osmosm입니다";
    public static final String NORMAL_PROFILE_MESSAGE2 = "안녕하세요2";

    public static final String NORMAL_EMAIL3 = "osm3@naver.com";
    public static final String NORMAL_USERNAME3 = "osm3입니다";
    public static final String NORMAL_PROFILE_MESSAGE3 = "안녕하세요3";

    public static final String UNNORMAL_EMAIL = "osm@";
    public static final String UNNORMAL_USERNAME = "o";

    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_EMAIL = "admin@gmail.com";

    public static final String EVENT_HOST_USERNAME = "eventHost";
    public static final String EVENT_HOST_EMAIL = "eventHost@gmail.com";


    //DB
    public static final String CONSTRAINT_UNIQUE_USERNAME = "UK__USER__NICKNAME";

    public static UserRegisterRequest getNormalUserRegisterRequest() {
        return UserRegisterRequest.builder()
                .nickname(NORMAL_USERNAME)
                .email(NORMAL_EMAIL)
                .roles(List.of(GeneralRoleType.USER.name()))
                .build();
    }

    public static UserModifyRequest getNormalUserModifyRequest() {
        return UserModifyRequest.builder()
                .username(JsonNullable.of(NORMAL_USERNAME))
                .profileMessage(JsonNullable.of(NORMAL_PROFILE_MESSAGE))
                .email(JsonNullable.of(NORMAL_EMAIL))
                .build();
    }

    public static UserRegisterRequest getNormal2UserRegisterRequest() {
        return UserRegisterRequest.builder()
                .nickname(NORMAL_USERNAME2)
                .email(NORMAL_EMAIL2)
                .roles(List.of(GeneralRoleType.USER.name()))
                .build();
    }

    public static UserModifyRequest getNormal2UserModifyRequest() {
        return UserModifyRequest.builder()
                .username(JsonNullable.of(NORMAL_USERNAME2))
                .profileMessage(JsonNullable.of(NORMAL_PROFILE_MESSAGE2))
                .email(JsonNullable.of(NORMAL_EMAIL2))
                .build();
    }

    public static UserRegisterRequest getNormal3UserRegisterRequest() {
        return UserRegisterRequest.builder()
                .nickname(NORMAL_USERNAME3)
                .email(NORMAL_EMAIL3)
                .roles(List.of(GeneralRoleType.USER.name()))
                .build();
    }

    public static UserModifyRequest getNormal3UserModifyRequest() {
        return UserModifyRequest.builder()
                .username(JsonNullable.of(NORMAL_USERNAME3))
                .profileMessage(JsonNullable.of(NORMAL_PROFILE_MESSAGE3))
                .email(JsonNullable.of(NORMAL_EMAIL3))
                .build();
    }

    public static UserRegisterRequest getUnNormalUserRegisterRequest() {
        return UserRegisterRequest.builder()
                .nickname(UNNORMAL_USERNAME)
                .email(UNNORMAL_EMAIL)
                .roles(List.of(GeneralRoleType.USER.name()))
                .build();
    }

    public static UserModifyRequest getUnNormalUserModifyRequest() {
        return UserModifyRequest.builder()
                .username(JsonNullable.of(UNNORMAL_USERNAME))
                .profileMessage(JsonNullable.of(NORMAL_PROFILE_MESSAGE))
                .email(JsonNullable.of(UNNORMAL_EMAIL))
                .build();
    }

    public static UserRegisterRequest getAdminUserRegisterRequest() {
        return UserRegisterRequest.builder()
                .nickname(ADMIN_USERNAME)
                .email(ADMIN_EMAIL)
                .roles(List.of(GeneralRoleType.ADMIN.name()))
                .build();
    }

    public static UserRegisterRequest getEventHostUserRegisterRequest() {
        return UserRegisterRequest.builder()
                .nickname(EVENT_HOST_USERNAME)
                .email(EVENT_HOST_EMAIL)
                .roles(List.of(GeneralRoleType.EVENT_HOST.name()))
                .build();
    }
}
