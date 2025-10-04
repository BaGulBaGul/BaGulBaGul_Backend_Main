package com.BaGulBaGul.BaGulBaGul.domain.user;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserInfoResponse;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTestUtils {
    public static void assertUserInfoResponse(UserInfoResponse userInfoResponse, User user) {
        assertThat(userInfoResponse.getId()).isEqualTo(user.getId());
        assertThat(userInfoResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(userInfoResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(userInfoResponse.getProfileMessage()).isEqualTo(user.getProfileMessage());
        assertThat(userInfoResponse.getImageURI()).isEqualTo(user.getImageURI());
    }
}
