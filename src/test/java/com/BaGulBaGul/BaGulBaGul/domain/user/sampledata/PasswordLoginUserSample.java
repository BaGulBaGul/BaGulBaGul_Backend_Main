package com.BaGulBaGul.BaGulBaGul.domain.user.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.PasswordLoginUserRegisterRequest;

public class PasswordLoginUserSample {

    public static String normalLoginId = "testtest";
    public static String normalLoginPassword = "testtestPW";
    public static String normal2LoginId = "testtest2";
    public static String normal2LoginPassword = "testtest2PW";
    public static String normal3LoginId = "testtest3";
    public static String normal3LoginPassword = "testtest3PW";
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

    public static PasswordLoginUserRegisterRequest getNormal3PasswordLoginUserRegisterRequest() {
        return PasswordLoginUserRegisterRequest.builder()
                .loginId(normal3LoginId)
                .loginPassword(normal3LoginPassword)
                .build();
    }
}
