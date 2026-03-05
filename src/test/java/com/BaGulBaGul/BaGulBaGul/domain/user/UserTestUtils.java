package com.BaGulBaGul.BaGulBaGul.domain.user;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.PasswordLoginUserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.EventHostUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.MyUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.OtherUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.constant.OAuth2Provider;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTestUtils {

    public static void assertUserInfoResponse(UserInfoResponse userInfoResponse, User user) {
        assertThat(userInfoResponse.getId()).isEqualTo(user.getId());
        assertThat(userInfoResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(userInfoResponse.getEmail()).isEqualTo(user.getEmail());
        assertThat(userInfoResponse.getProfileMessage()).isEqualTo(user.getProfileMessage());
        assertThat(userInfoResponse.getImageURI()).isEqualTo(user.getImageURI());
    }

    public static void assertMyUserInfoResponse(
            MyUserInfoResponse myUserInfoResponse,
            User user,
            Long writingCount,
            Long postLikeCount,
            Long calendarCount
    ) {
        assertUserInfoResponse(myUserInfoResponse, user);
        if(writingCount != null) {
            assertThat(myUserInfoResponse.getWritingCount()).isEqualTo(writingCount);
        }
        if(postLikeCount != null) {
            assertThat(myUserInfoResponse.getWritingCount()).isEqualTo(postLikeCount);
        }
        if(calendarCount != null) {
            assertThat(myUserInfoResponse.getCalendarCount()).isEqualTo(calendarCount);
        }
    }

    public static void assertOtherUserInfoResponse(
            OtherUserInfoResponse otherUserInfoResponse,
            User user,
            Long writingCount
    ) {
        assertUserInfoResponse(otherUserInfoResponse, user);
        if(writingCount != null) {
            assertThat(otherUserInfoResponse.getWritingCount()).isEqualTo(writingCount);
        }
    }

    public static void assertEventHostUserInfoResponse(
            EventHostUserInfoResponse eventHostUserInfoResponse,
            User user,
            Long festivalCount,
            Long localEventCount,
            Long partyCount
    ) {
        assertUserInfoResponse(eventHostUserInfoResponse, user);
        if(festivalCount != null) {
            assertThat(eventHostUserInfoResponse.getFestivalCount()).isEqualTo(festivalCount);
        }
        if(localEventCount != null) {
            assertThat(eventHostUserInfoResponse.getLocalEventCount()).isEqualTo(localEventCount);
        }
        if(partyCount != null) {
            assertThat(eventHostUserInfoResponse.getPartyCount()).isEqualTo(partyCount);
        }
    }

    public static void assertUserRegister(
            User user, UserRegisterRequest userRegisterRequest
    ) {
        assertThat(user.getNickname()).isEqualTo(userRegisterRequest.getNickname());
        assertThat(user.getEmail()).isEqualTo(userRegisterRequest.getEmail());
        assertThat(user.getUserRoles().stream().map(userRole -> userRole.getRole().getName()))
                .containsExactlyInAnyOrder(userRegisterRequest.getRoles().toArray(String[]::new));
    }

    public static void assertPasswordLoginUserRegister(
            PasswordLoginUser passwordLoginUser,
            PasswordLoginUserRegisterRequest passwordLoginUserRegisterRequest,
            PasswordEncoder passwordEncoder
    ) {
        assertThat(passwordLoginUser.getLoginId()).isEqualTo(passwordLoginUserRegisterRequest.getLoginId());
        assertThat(passwordEncoder.matches(
                passwordLoginUserRegisterRequest.getLoginPassword(), passwordLoginUser.getEncodedLoginPassword())
        ).isTrue();
    }

    public static void assertSocialLoginUserRegister(
            SocialLoginUser socialLoginUser,
            String socialLoginUserId,
            OAuth2Provider oAuth2Provider
    ) {
        assertThat(socialLoginUser.getId()).isEqualTo(socialLoginUserId);
        assertThat(socialLoginUser.getProvider()).isEqualTo(oAuth2Provider);
    }
}
