package com.BaGulBaGul.BaGulBaGul.domain.user.info.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.BaGulBaGul.BaGulBaGul.domain.user.alarm.repository.UserAlarmStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.dto.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.exception.DuplicateUsernameException;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.SocialLoginUserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class UserJoinService_UnitTest {

    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private SocialLoginUserRepository socialLoginUserRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserAlarmStatusRepository userAlarmStatusRepository;
    @Mock
    private UserImageService userImageService;
    @InjectMocks
    private UserJoinServiceImpl userJoinService;

    @Nested
    @DisplayName("유저 등록 테스트")
    class registerUserTest {
        @Test
        @DisplayName("유저명 유니크 제약조건 오류 시 DuplicateUsernameException을 던져야 함")
        void shouldThrowDuplicateUsernameException_WhenViolentUniqueConstraint() {
            //given
            when(userRepository.save(any())).thenThrow(new DataIntegrityViolationException(UserSample.CONSTRAINT_UNIQUE_USERNAME));
            //when
            //then
            assertThatThrownBy(() -> userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST))
                    .isInstanceOf(DuplicateUsernameException.class);
        }
    }
}