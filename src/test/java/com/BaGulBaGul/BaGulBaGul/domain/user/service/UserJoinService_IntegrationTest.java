package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.DuplicateUsernameException;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserJoinService_IntegrationTest {

    @Autowired
    UserJoinService userJoinService;

    @Nested
    @DisplayName("유저 등록 테스트")
    class registerUserTest {
        @Test
        @DisplayName("정상 동작")
        @Transactional
        void shouldOK() {
            userJoinService.registerUser(
                    UserSample.getNormalUserRegisterRequest()
            );
        }

        @Test
        @DisplayName("유저명이 기존 유저의 유저명과 정확히 일치")
        @Transactional
        void shouldThrowDuplicateUsernameException_WhenExactlySame() {
            //given
            UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
            userJoinService.registerUser(
                    userRegisterRequest
            );
            //when then
            assertThatThrownBy(() -> userJoinService.registerUser(userRegisterRequest))
                    .isInstanceOf(DuplicateUsernameException.class);
        }

        @Test
        @DisplayName("유저명이 기존 유저의 유저명과 대소문자 관계 없이 일치")
        @Transactional
        void shouldThrowDuplicateUsernameException_WhenCaseInsensitive() {
            //given
            UserRegisterRequest lowerCaseUsernameRequest = UserRegisterRequest.builder()
                    .nickname(UserSample.NORMAL_USERNAME)
                    .email(UserSample.NORMAL_EMAIL)
                    .build();
            UserRegisterRequest upperCaseUsernameRequest = UserRegisterRequest.builder()
                    .nickname(UserSample.NORMAL_USERNAME_UPPERCASE)
                    .email(UserSample.NORMAL_EMAIL)
                    .build();
            userJoinService.registerUser(lowerCaseUsernameRequest);
            //when then
            assertThatThrownBy(() -> userJoinService.registerUser(upperCaseUsernameRequest))
                    .isInstanceOf(DuplicateUsernameException.class);
        }
    }

    @Nested
    @DisplayName("유저명 중복 확인 메서드 테스트")
    class checkDuplicateUsernameTest {
        @Test
        @DisplayName("유저명이 기존 유저의 유저명과 정확히 일치")
        @Transactional
        void shouldDuplicateUsername_WhenMatchExactly() {
            //given
            userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            //when
            boolean isDuplicate = userJoinService.checkDuplicateUsername(UserSample.NORMAL_USERNAME);
            //then
            assertThat(isDuplicate).isTrue();
        }

        @Test
        @DisplayName("유저명이 기존 유저의 유저명과 대소문자 관계 없이 일치")
        @Transactional
        void shouldDuplicateUsername_WhenMatchLowerCase() {
            //given
            userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            //when
            boolean isDuplicate = userJoinService.checkDuplicateUsername(UserSample.NORMAL_USERNAME_UPPERCASE);
            //then
            assertThat(isDuplicate).isTrue();
        }

        @Test
        @DisplayName("유저명이 중복되지 않는 경우")
        @Transactional
        void shouldDuplicateUsername_WhenNotMatch() {
            //given when
            boolean isDuplicate = userJoinService.checkDuplicateUsername(UserSample.NORMAL_USERNAME);
            //then
            assertThat(isDuplicate).isFalse();
        }
    }
}
