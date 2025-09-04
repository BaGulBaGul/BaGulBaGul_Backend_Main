package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.LiftUserSuspensionApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.SuspendUserApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserSuspensionStatus;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserSuspensionStatusRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserSuspensionService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserSuspensionAdminController_IntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserJoinService userJoinService;

    @Autowired
    UserSuspensionService userSuspensionService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserSuspensionStatusRepository userSuspensionStatusRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Value("${user.login.access_token_cookie_name}")
    private String ACCESS_TOKEN_COOKIE_NAME;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(post("/").with(csrf()))
                .build();
    }

    private String getAdminToken() {
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        return jwtProvider.createAccessToken(admin.getId()).getJwt();
    }

    private String getNormalUserToken(User user) {
        return jwtProvider.createAccessToken(user.getId()).getJwt();
    }

    @Test
    @DisplayName("관리자가 사용자를 정지시킨다.")
    void suspendUser_success() throws Exception {
        //given
        String adminToken = getAdminToken();
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        SuspendUserApiRequest request = new SuspendUserApiRequest(
                "test reason",
                LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusDays(7)
        );

        //when
        //then
        mockMvc.perform(post("/api/admin/user/suspension/{userId}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken)))
                .andExpect(status().isOk())
                .andDo(print());

        User suspendedUser = userRepository.findById(user.getId()).get();
        assertThat(suspendedUser.isSuspended()).isTrue();

        UserSuspensionStatus status = userSuspensionStatusRepository.findById(user.getId()).get();
        assertThat(status.getReason()).isEqualTo(request.getReason());
        assertThat(status.getEndDate()).isEqualTo(request.getEndDate());
    }

    @Test
    @DisplayName("endDate가 시간 단위로 잘려있지 않으면 400에러가 발생한다.")
    void suspendUser_fail_if_endDate_not_truncated() throws Exception {
        //given
        String adminToken = getAdminToken();
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        endDate = endDate.withMinute(1);
        SuspendUserApiRequest request = new SuspendUserApiRequest(
                "test reason",
                endDate
        );

        //when
        //then
        mockMvc.perform(post("/api/admin/user/suspension/{userId}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ResponseCode.BAD_REQUEST.getCode()))
                .andDo(print());
    }

    @Test
    @DisplayName("관리자가 아닌 유저가 정지를 요청하면 403에러가 발생한다.")
    void suspendUser_fail_if_not_admin() throws Exception {
        //given
        User normalUser = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        String normalUserToken = getNormalUserToken(normalUser);
        User userToSuspend = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());

        SuspendUserApiRequest request = new SuspendUserApiRequest(
                "test reason",
                LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusDays(7)
        );

        //when
        //then
        mockMvc.perform(post("/api/admin/user/suspension/{userId}", userToSuspend.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, normalUserToken)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("관리자가 사용자의 정지를 해제한다.")
    void liftSuspension_success() throws Exception {
        //given
        String adminToken = getAdminToken();
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        userSuspensionService.suspendUser(user.getId(), user.getId(), new SuspendUserRequest("test reason", LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusDays(7)));
        LiftUserSuspensionApiRequest request = new LiftUserSuspensionApiRequest("lift reason");

        //when
        //then
        mockMvc.perform(delete("/api/admin/user/suspension/{userId}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken)))
                .andExpect(status().isOk())
                .andDo(print());

        User unsuspendedUser = userRepository.findById(user.getId()).get();
        assertThat(unsuspendedUser.isSuspended()).isFalse();

        UserSuspensionStatus status = userSuspensionStatusRepository.findById(user.getId()).get();
        assertThat(status.getEndDate()).isNull();
        assertThat(status.getReason()).isNull();
    }

    @Test
    @DisplayName("관리자가 아닌 유저가 정지를 해제하려하면 403에러가 발생한다.")
    void liftSuspension_fail_if_not_admin() throws Exception {
        //given
        User normalUser = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        String normalUserToken = getNormalUserToken(normalUser);
        User userToLift = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());

        LiftUserSuspensionApiRequest request = new LiftUserSuspensionApiRequest("lift reason");

        //when
        //then
        mockMvc.perform(delete("/api/admin/user/suspension/{userId}", userToLift.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, normalUserToken)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("정지되지 않은 유저를 해제하려하면 예외가 발생한다.")
    void liftSuspension_fail_if_user_not_suspended() throws Exception {
        //given
        String adminToken = getAdminToken();
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());

        LiftUserSuspensionApiRequest request = new LiftUserSuspensionApiRequest("lift reason");

        //when
        //then
        mockMvc.perform(delete("/api/admin/user/suspension/{userId}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ResponseCode.ADMIN_USER_NOT_SUSPENDED.getCode()))
                .andDo(print());
    }
}