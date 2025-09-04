package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.BaGulBaGul.BaGulBaGul.domain.user.PasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.PasswordLoginUserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.PasswordLoginUserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.PasswordLoginUserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class PasswordLoginUserServiceImpl_IntegrationTest {

    @Autowired
    PasswordLoginUserRepository passwordLoginUserRepository;

    @Autowired
    PasswordLoginUserServiceImpl passwordLoginUserService;

    @Autowired
    UserJoinService userJoinService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    void createTest() {
        //when
        PasswordLoginUserRegisterRequest normalPasswordLoginUserRegisterRequest = PasswordLoginUserSample.getNormalPasswordLoginUserRegisterRequest();
        UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
        User user = userJoinService.registerUser(userRegisterRequest);
        PasswordLoginUser passwordLoginUser = passwordLoginUserService.registerPasswordLoginUser(
                normalPasswordLoginUserRegisterRequest,
                user
        );
        //then
        String rawPW = normalPasswordLoginUserRegisterRequest.getLoginPassword();
        assertThat(passwordLoginUser.getLoginId()).isEqualTo(normalPasswordLoginUserRegisterRequest.getLoginId());
        assertTrue(passwordEncoder.matches(rawPW, passwordLoginUser.getEncodedLoginPassword()));
    }

    @Test
    @Transactional
    void deleteTest() {
        //given
        PasswordLoginUserRegisterRequest normalPasswordLoginUserRegisterRequest = PasswordLoginUserSample.getNormalPasswordLoginUserRegisterRequest();
        UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
        User user = userJoinService.registerUser(userRegisterRequest);
        PasswordLoginUser passwordLoginUser = passwordLoginUserService.registerPasswordLoginUser(
                normalPasswordLoginUserRegisterRequest,
                user
        );
        //when
        passwordLoginUserService.deletePasswordLoginUser(passwordLoginUser.getLoginId());
        //then
        PasswordLoginUser findResult = passwordLoginUserRepository.findById(passwordLoginUser.getLoginId())
                .orElse(null);
        assertThat(findResult).isNull();
    }

    @Test
    @Transactional
    void findTest() {
        //given
        PasswordLoginUserRegisterRequest normalPasswordLoginUserRegisterRequest = PasswordLoginUserSample.getNormalPasswordLoginUserRegisterRequest();
        UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
        User user = userJoinService.registerUser(userRegisterRequest);
        PasswordLoginUser passwordLoginUser = passwordLoginUserService.registerPasswordLoginUser(
                normalPasswordLoginUserRegisterRequest,
                user
        );
        //when
        String loginId = normalPasswordLoginUserRegisterRequest.getLoginId();
        String loginPassword = normalPasswordLoginUserRegisterRequest.getLoginPassword();
        PasswordLoginUser findResult = passwordLoginUserService.findPasswordLoginUser(loginId, loginPassword);
        //then
        assertThat(findResult.getLoginId()).isEqualTo(loginId);
        assertTrue(passwordEncoder.matches(loginPassword, findResult.getEncodedLoginPassword()));
    }
}