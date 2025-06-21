package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.DuplicateUsernameException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class UserInfoService_IntegrationTest {

    @MockBean
    UserImageService userImageService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserJoinService userJoinService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void init() {
        doNothing().when(userImageService).setImage(any(), any());
    }

    @Nested
    @DisplayName("유저 수정 테스트")
    class modifyUserInfoTest {

        @Test
        @DisplayName("정상 동작")
        @Transactional
        void shouldOK() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            UserModifyRequest normal2UserModifyRequest = UserSample.getNormal2UserModifyRequest();
            normal2UserModifyRequest.setImageResourceId(JsonNullable.of(1L));
            //when
            userInfoService.modifyUserInfo(normal2UserModifyRequest, user.getId());
            //수정한 내용을 db에 즉시 반영하고 영속성 컨텍스트 초기화
            entityManager.flush();
            entityManager.clear();
            user = userRepository.findById(user.getId()).orElse(null);

            //then
            assertThat(user.getNickname()).isEqualTo(normal2UserModifyRequest.getUsername().get());
            assertThat(user.getEmail()).isEqualTo(normal2UserModifyRequest.getEmail().get());
            assertThat(user.getProfileMessage()).isEqualTo(normal2UserModifyRequest.getProfileMessage().get());
            verify(userImageService, times(1)).setImage(any(), any());
        }

        @Test
        @DisplayName("유저명이 기존 유저의 유저명과 정확히 일치")
        @Transactional
        void shouldThrowDuplicateUsernameException_WhenUsernameExactlySame() {
            //given
            UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
            UserModifyRequest userModifyRequest = UserSample.getNormal2UserModifyRequest();
            userModifyRequest.setUsername(JsonNullable.of(userRegisterRequest.getNickname()));
            User user = userJoinService.registerUser(userRegisterRequest);
            //when then
            assertThatThrownBy(() -> {
                userInfoService.modifyUserInfo(userModifyRequest, user.getId());
                //수정한 내용을 db에 즉시 반영하고 영속성 컨텍스트 초기화
                entityManager.flush();
                entityManager.clear();
            }).isInstanceOf(DuplicateUsernameException.class);
        }

        @Test
        @DisplayName("유저명이 기존 유저의 유저명과 대소문자 관계 없이 일치")
        @Transactional
        void shouldThrowDuplicateUsernameException_WhenUsernameCaseInsensitiveSame() {
            //given
            UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
            userRegisterRequest.setNickname(userRegisterRequest.getNickname().toLowerCase());
            UserModifyRequest userModifyRequest = UserSample.getNormal2UserModifyRequest();
            userModifyRequest.setUsername(JsonNullable.of(userRegisterRequest.getNickname().toUpperCase()));
            User user = userJoinService.registerUser(userRegisterRequest);
            //when then
            assertThatThrownBy(() -> {
                userInfoService.modifyUserInfo(userModifyRequest, user.getId());
                //수정한 내용을 db에 즉시 반영하고 영속성 컨텍스트 초기화
                entityManager.flush();
                entityManager.clear();
            }).isInstanceOf(DuplicateUsernameException.class);
        }
    }
}