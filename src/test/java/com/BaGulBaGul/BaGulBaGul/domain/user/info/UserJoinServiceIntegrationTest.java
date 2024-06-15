package com.BaGulBaGul.BaGulBaGul.domain.user.info;

import static org.assertj.core.api.Assertions.assertThat;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.service.UserJoinService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
@ActiveProfiles("test")
public class UserJoinServiceIntegrationTest {

    @Autowired
    UserJoinService userJoinService;

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
