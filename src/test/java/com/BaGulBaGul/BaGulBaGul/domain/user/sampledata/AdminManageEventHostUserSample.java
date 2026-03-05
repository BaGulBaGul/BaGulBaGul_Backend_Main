package com.BaGulBaGul.BaGulBaGul.domain.user.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManageEventHostUserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManageEventHostUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserRegisterRequest;
import org.openapitools.jackson.nullable.JsonNullable;

public class AdminManageEventHostUserSample {

    public static final String NORMAL_NICKNAME = "ADMEHUser";
    public static final String NORMAL_EMAIL = "test@naver.com";

    public static final String NORMAL2_NICKNAME = "ADMEHUser2";
    public static final String NORMAL2_EMAIL = "test2@naver.com";

    public static AdminManageEventHostUserJoinRequest getNormalAdminManageEventHostUserRegisterRequest() {
        return AdminManageEventHostUserJoinRequest.builder()
                .userRegisterRequest(UserRegisterRequest.builder()
                        .nickname(NORMAL_NICKNAME)
                        .email(NORMAL_EMAIL)
                        .build())
                .build();
    }

    public static AdminManageEventHostUserModifyRequest getNormalAdminManageEventHostUserModifyRequest() {
        return AdminManageEventHostUserModifyRequest.builder()
                .userModifyRequest(UserModifyRequest.builder()
                        .username(JsonNullable.of(NORMAL_NICKNAME))
                        .email(JsonNullable.of(NORMAL_EMAIL))
                        .build())
                .build();
    }

    public static AdminManageEventHostUserJoinRequest getNormal2AdminManageEventHostUserRegisterRequest() {
        return AdminManageEventHostUserJoinRequest.builder()
                .userRegisterRequest(UserRegisterRequest.builder()
                        .nickname(NORMAL2_NICKNAME)
                        .email(NORMAL2_EMAIL)
                        .build())
                .build();
    }

    public static AdminManageEventHostUserModifyRequest getNormal2AdminManageEventHostUserModifyRequest() {
        return AdminManageEventHostUserModifyRequest.builder()
                .userModifyRequest(UserModifyRequest.builder()
                        .username(JsonNullable.of(NORMAL2_NICKNAME))
                        .email(JsonNullable.of(NORMAL2_EMAIL))
                        .build())
                .build();
    }
}
