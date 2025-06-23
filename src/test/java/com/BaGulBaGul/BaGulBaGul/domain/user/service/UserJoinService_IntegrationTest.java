package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManageEventHostUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.SocialLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.requset.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.DuplicateUsernameException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.AdminManageEventHostUserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.SocialLoginUserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.AdminManageEventHostUserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.SocialLoginUserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.constant.OAuth2Provider;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.dto.OAuth2JoinTokenSubject;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import javax.persistence.EntityManager;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminManageEventHostUserRepository adminManageEventHostUserRepository;

    @Autowired
    SocialLoginUserRepository socialLoginUserRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    EntityManager entityManager;

//    @AfterEach
//    void tearDown() {
//        List<User> users = userRepository.findAll();
//        for(User user : users) {
//            userJoinService.deleteUser(user.getId());
//        }
//    }

    @Nested
    @DisplayName("유저 등록 테스트")
    class registerUserTest {
        @Test
        @DisplayName("정상 동작")
        @Transactional
        void shouldOK() {
            //given
            UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
            //when
            User user = userJoinService.registerUser(
                    userRegisterRequest
            );
            //then
            assertThat(user.getEmail()).isEqualTo(userRegisterRequest.getEmail());
            assertThat(user.getNickname()).isEqualTo(userRegisterRequest.getNickname());
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
            UserRegisterRequest lowerCaseUsernameRequest = UserSample.getNormalUserRegisterRequest();
            lowerCaseUsernameRequest.setNickname(lowerCaseUsernameRequest.getNickname().toLowerCase());
            UserRegisterRequest upperCaseUsernameRequest = UserSample.getNormal2UserRegisterRequest();
            upperCaseUsernameRequest.setNickname(lowerCaseUsernameRequest.getNickname().toUpperCase());
            userJoinService.registerUser(lowerCaseUsernameRequest);
            //when then
            assertThatThrownBy(() -> userJoinService.registerUser(upperCaseUsernameRequest))
                    .isInstanceOf(DuplicateUsernameException.class);
        }
    }

    @Nested
    @DisplayName("유저 삭제 테스트")
    class deleteUserTest {
        @Test
        @DisplayName("정상 삭제")
        @Transactional
        void shouldOK() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            //when
            userJoinService.deleteUser(user.getId());
            //then
            User findUser = userRepository.findById(user.getId()).orElse(null);
            assertThat(findUser).isNull();
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
            UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
            userJoinService.registerUser(userRegisterRequest);
            //when
            boolean isDuplicate = userJoinService.checkDuplicateUsername(userRegisterRequest.getNickname());
            //then
            assertThat(isDuplicate).isTrue();
        }

        @Test
        @DisplayName("유저명이 기존 유저의 유저명과 대소문자 관계 없이 일치")
        @Transactional
        void shouldDuplicateUsername_WhenMatchLowerCase() {
            //given
            UserRegisterRequest lowerCaseUsernameRequest = UserSample.getNormalUserRegisterRequest();
            lowerCaseUsernameRequest.setNickname(lowerCaseUsernameRequest.getNickname().toLowerCase());
            userJoinService.registerUser(lowerCaseUsernameRequest);
            //when
            boolean isDuplicate = userJoinService.checkDuplicateUsername(lowerCaseUsernameRequest.getNickname().toUpperCase());
            //then
            assertThat(isDuplicate).isTrue();
        }

        @Test
        @DisplayName("유저명이 중복되지 않는 경우")
        @Transactional
        void shouldNotDuplicateUsername() {
            //given when
            UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
            boolean isDuplicate = userJoinService.checkDuplicateUsername(userRegisterRequest.getNickname());
            //then
            assertThat(isDuplicate).isFalse();
        }
    }

    @Nested
    @DisplayName("소셜 로그인 유저 등록 테스트")
    class registerSocialLoginUserTest {
        @Test
        @DisplayName("정상 등록")
        @Transactional
        void shouldOk() {
            //given
            SocialLoginUserJoinRequest socialLoginUserJoinRequest = SocialLoginUserSample
                    .getNormalSocialLoginUserJoinRequest();
            String socialLoginUserId = "xxxx";
            OAuth2Provider oauthProvider = OAuth2Provider.kakao;
            String oAuth2JoinToken = jwtProvider.createOAuth2JoinToken(
                    OAuth2JoinTokenSubject.builder()
                            .socialLoginId(socialLoginUserId)
                            .oAuth2Provider(oauthProvider)
                            .build()
            );
            socialLoginUserJoinRequest.setJoinToken(oAuth2JoinToken);
            //when
            SocialLoginUser socialLoginUser = userJoinService.registerSocialLoginUser(socialLoginUserJoinRequest);
            //then
            assertThat(socialLoginUser.getId()).isEqualTo(socialLoginUserId);
            assertThat(socialLoginUser.getProvider()).isEqualTo(oauthProvider);
        }
    }

    @Nested
    @DisplayName("소셜 로그인 유저 삭제 테스트")
    class deleteSocialLoginUserTest {
        @Test
        @DisplayName("정상 삭제")
        @Transactional
        void shouldOk() {
            //given
            SocialLoginUserJoinRequest socialLoginUserJoinRequest = SocialLoginUserSample
                    .getNormalSocialLoginUserJoinRequest();
            String socialLoginUserId = "xxxx";
            OAuth2Provider oauthProvider = OAuth2Provider.kakao;
            String oAuth2JoinToken = jwtProvider.createOAuth2JoinToken(
                    OAuth2JoinTokenSubject.builder()
                            .socialLoginId(socialLoginUserId)
                            .oAuth2Provider(oauthProvider)
                            .build()
            );
            socialLoginUserJoinRequest.setJoinToken(oAuth2JoinToken);
            SocialLoginUser socialLoginUser = userJoinService.registerSocialLoginUser(
                    socialLoginUserJoinRequest
            );
            Long userId = socialLoginUser.getUser().getId();
            //when
            userJoinService.deleteUser(userId);


            //then
            User findUser = userRepository.findById(userId).orElse(null);
            SocialLoginUser findsocialLoginUser = socialLoginUserRepository.findById(socialLoginUserId).orElse(null);
            assertThat(findUser).isNull();
            assertThat(findsocialLoginUser).isNull();
        }
    }

    @Nested
    @DisplayName("관리자 관리 이벤트 호스트 유저 등록 테스트")
    class registerAdminManageEventHostUserTest {
        @Test
        @DisplayName("정상 등록")
        @Transactional
        void shouldOk() {
            //given when
            AdminManageEventHostUser adminManageEventHostUser = userJoinService.registerAdminManageEventHostUser(
                    AdminManageEventHostUserSample.getNormalAdminManageEventHostUserRegisterRequest()
            );
            //then
            Long adminManageEventHostUserId = adminManageEventHostUser.getId();
            Long userId = adminManageEventHostUser.getUser().getId();
            AdminManageEventHostUser findAdminManageEventHostUser = adminManageEventHostUserRepository
                    .findById(adminManageEventHostUserId).orElse(null);
            User findUser = userRepository.findById(userId).orElse(null);
            assertThat(findAdminManageEventHostUser).isNotNull();
            assertThat(findUser).isNotNull();
        }
    }

    @Nested
    @DisplayName("관리자 관리 이벤트 호스트 유저 삭제 테스트")
    class deleteAdminManageEventHostUserTest {
        @Test
        @DisplayName("정상 삭제")
        @Transactional
        void shouldOk() {
            //given
            AdminManageEventHostUser adminManageEventHostUser = userJoinService.registerAdminManageEventHostUser(
                    AdminManageEventHostUserSample.getNormalAdminManageEventHostUserRegisterRequest()
            );
            Long adminManageEventHostUserId = adminManageEventHostUser.getId();
            Long userId = adminManageEventHostUser.getUser().getId();

            //when
            userJoinService.deleteAdminManageEventHostUser(adminManageEventHostUserId);

            //then
            User findUser = userRepository.findById(userId).orElse(null);
            AdminManageEventHostUser findAdminManageEventHostUser = adminManageEventHostUserRepository
                    .findById(adminManageEventHostUserId).orElse(null);
            assertThat(findUser).isNull();
            assertThat(findAdminManageEventHostUser).isNull();
        }
    }
}
