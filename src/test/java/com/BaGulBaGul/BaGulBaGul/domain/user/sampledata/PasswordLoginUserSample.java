package com.BaGulBaGul.BaGulBaGul.domain.user.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.PasswordLoginUserRegisterRequest;

public class PasswordLoginUserSample {

    public static String normalLoginId = "test";
    public static String normalLoginPassword = "testPW";
    public static String normal2LoginId = "test2";
    public static String normal2LoginPassword = "test2PW";

    public static PasswordLoginUserRegisterRequest getNormalPasswordLoginUserRegisterRequest() {
        return PasswordLoginUserRegisterRequest.builder()
                .loginId(normalLoginId)
                .loginPassword(normalLoginPassword)
                .build();
    }

    public static PasswordLoginUserRegisterRequest getNormal2PasswordLoginUserRegisterRequest() {
        return PasswordLoginUserRegisterRequest.builder()
                .loginId(normal2LoginId)
                .loginPassword(normal2LoginPassword)
                .build();
    }
}
