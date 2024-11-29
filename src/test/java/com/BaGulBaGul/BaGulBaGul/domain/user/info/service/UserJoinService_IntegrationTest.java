package com.BaGulBaGul.BaGulBaGul.domain.user.info.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.repository.UserAlarmStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.exception.DuplicateUsernameException;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.SocialLoginUserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test2")
public class UserJoinService_IntegrationTest {

    @Autowired
    UserJoinService userJoinService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAlarmStatusRepository userAlarmStatusRepository;

    @Autowired
    SocialLoginUserRepository socialLoginUserRepository;

    @Nested
    @DisplayName("유저 등록 테스트")
    class registerUserTest {
        @AfterEach
        void clear() {
            userRepository.deleteAll();
            userAlarmStatusRepository.deleteAll();
            socialLoginUserRepository.deleteAll();
        }

        @Test
        @DisplayName("정상 동작")
        void shouldOK() {
            userJoinService.registerUser(
                    UserSample.NORMAL_USER_REGISTER_REQUEST
            );
        }

        @Test
        @DisplayName("유저명이 기존 유저의 유저명과 정확히 일치")
        void shouldThrowDuplicateUsernameException_WhenExactlySame() {
            //given
            UserRegisterRequest userRegisterRequest = UserSample.NORMAL_USER_REGISTER_REQUEST;
            userJoinService.registerUser(
                    userRegisterRequest
            );
            //when then
            assertThatThrownBy(() -> userJoinService.registerUser(userRegisterRequest))
                    .isInstanceOf(DuplicateUsernameException.class);
        }

        @Test
        @DisplayName("유저명이 기존 유저의 유저명과 대소문자 관계 없이 일치")
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
    @TestInstance(Lifecycle.PER_CLASS)
    class checkDuplicateUsernameTest {
        String targetUsername = "TestUser1";
        String targetUsernameLowerCase = "testuser1";
        User testuser;
        @BeforeAll
        void init() {
            testuser = userJoinService.registerUser(
                    UserRegisterRequest.builder()
                            .email("test")
                            .nickname(targetUsername)
                            .build()
            );
        }
        @AfterAll
        void clear() {
            userJoinService.deleteUser(testuser.getId());
        }

        @Test
        @DisplayName("유저명이 기존 유저의 유저명과 정확히 일치")
        void shouldDuplicateUsername_WhenMatchExactly() {
            boolean isDuplicate = userJoinService.checkDuplicateUsername(targetUsername);
            assertThat(isDuplicate).isTrue();
        }

        @Test
        @DisplayName("유저명이 기존 유저의 유저명과 대소문자 관계 없이 일치")
        void shouldDuplicateUsername_WhenMatchLowerCase() {
            boolean isDuplicate = userJoinService.checkDuplicateUsername(targetUsernameLowerCase);
            assertThat(isDuplicate).isTrue();
        }

        @Test
        @DisplayName("유저명이 중복되지 않는 경우")
        void shouldDuplicateUsername_WhenNotMatch() {
            boolean isDuplicate = userJoinService.checkDuplicateUsername("NotExistsUser1");
            assertThat(isDuplicate).isFalse();
        }
    }
}
