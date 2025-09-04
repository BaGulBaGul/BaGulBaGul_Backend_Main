package com.BaGulBaGul.BaGulBaGul.domain.user.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.PasswordLoginUserRegisterRequest;

public class PasswordLoginUserSample {

    public static String normalLoginId = "test";
    public static String normalLoginPassword = "testPW";

    public static PasswordLoginUserRegisterRequest getNormalPasswordLoginUserRegisterRequest() {
        return PasswordLoginUserRegisterRequest.builder()
                .loginId(normalLoginId)
                .loginPassword(normalLoginPassword)
                .build();
    }
}
