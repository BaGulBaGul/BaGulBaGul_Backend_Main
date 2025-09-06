package com.BaGulBaGul.BaGulBaGul.global.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.PasswordLoginUserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.PasswordLoginUserService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserRoleService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserSuspensionService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AccessTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.LoginApiRequest;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.RefreshTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtStorageService;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserAuthController_IntegrationTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    JwtProvider jwtProvider;

    @SpyBean
    JwtStorageService jwtStorageService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    UserJoinService userJoinService;

    @Autowired
    PasswordLoginUserService passwordLoginUserService;

    @Autowired
    UserSuspensionService userSuspensionService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${user.login.access_token_cookie_name}")
    private String ACCESS_TOKEN_COOKIE_NAME;

    @Value("${user.login.refresh_token_cookie_name}")
    private String REFRESH_TOKEN_COOKIE_NAME;

    @Value("${user.login.refresh_token_expire_minute}")
    private int REFRESH_TOKEN_EXPIRE_MINUTE;

    @Value("${user.login.access_token_expire_minute}")
    private int ACCESS_TOKEN_EXPIRE_MINUTE;

    @BeforeEach
    void setup(WebApplicationContext ctx) {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .apply(springSecurity())
                .defaultRequest(get("/").with(csrf()))
                .build();
    }

    @AfterEach
    void tearDown() {
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }

    @Nested
    @DisplayName("로그인 테스트")
    class LoginTest {

        private final String path = "/api/auth/login/password";

        @Test
        @DisplayName("정상 패스워드 로그인 테스트")
        @Transactional
        void shouldOk_WhenNormalPasswordLoginUser() throws Exception {
            //given
            UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
            User user = userJoinService.registerUser(userRegisterRequest);
            String password = "password";
            passwordLoginUserService.registerPasswordLoginUser(
                    new PasswordLoginUserRegisterRequest(userRegisterRequest.getEmail(), password),
                    user
            );
            LoginApiRequest loginApiRequest = new LoginApiRequest(userRegisterRequest.getEmail(), password);

            int rtExpireSecond = REFRESH_TOKEN_EXPIRE_MINUTE * 60;
            //AT는 재발급 시 검증을 위해 RT 만료 전까지 쿠키에 살아있어야 한다.
            int atExpireSecond = rtExpireSecond;

            //when
            ResultActions resultActions = mvc.perform(post(path)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginApiRequest))
            );

            //then
            ResponseCode responseCode = ResponseCode.SUCCESS;
            resultActions.andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()))
                    .andExpectAll(
                            cookie().exists(ACCESS_TOKEN_COOKIE_NAME),
                            cookie().path(ACCESS_TOKEN_COOKIE_NAME, "/"),
                            cookie().maxAge(ACCESS_TOKEN_COOKIE_NAME, atExpireSecond)
                    )
                    .andExpectAll(
                            cookie().exists(REFRESH_TOKEN_COOKIE_NAME),
                            cookie().path(REFRESH_TOKEN_COOKIE_NAME, "/api/auth/refresh"),
                            cookie().maxAge(REFRESH_TOKEN_COOKIE_NAME, rtExpireSecond)
                    );
            verify(jwtStorageService, times(1)).save(any(), any());
        }

        @Test
        @DisplayName("정지된 패스워드 로그인 유저 로그인 테스트")
        @Transactional
        void should403_WhenSuspendedPasswordLoginUser() throws Exception {
            //given
            //정지할 유저 생성
            UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
            User user = userJoinService.registerUser(userRegisterRequest);
            String password = "password";
            passwordLoginUserService.registerPasswordLoginUser(
                    new PasswordLoginUserRegisterRequest(userRegisterRequest.getEmail(), password),
                    user
            );

            //관리자 유저 생성
            UserRegisterRequest adminRegisterRequest = UserSample.getAdminUserRegisterRequest();
            User admin = userJoinService.registerUser(adminRegisterRequest);

            //정지 요청 생성
            LocalDateTime endDate = LocalDateTime.now().plusDays(1).withNano(0);
            String reason = "test";
            SuspendUserRequest suspendUserRequest = new SuspendUserRequest(reason, endDate);
            //정지
            userSuspensionService.suspendUser(admin.getId(), user.getId(), suspendUserRequest);
            //로그인 요청 생성
            LoginApiRequest loginApiRequest = new LoginApiRequest(userRegisterRequest.getEmail(), password);

            //when
            ResultActions resultActions = mvc.perform(post(path)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginApiRequest))
            );

            //then
            ResponseCode responseCode = ResponseCode.AUTH_SUSPENDED_USER;
            resultActions.andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()))
                    .andExpect(jsonPath("$.data.endDate").value(endDate.toString()))
                    .andExpect(jsonPath("$.data.reason").value(reason));
        }
    }

    @Nested
    @DisplayName("로그아웃 테스트")
    class LogoutTest {

        private static final String path = "/api/auth/logout";

        @Test
        @DisplayName("정상 AT 테스트")
        @Transactional
        void shouldOk_WhenNormalAt() throws Exception {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            Long userId = user.getId();
            AccessTokenInfo atInfo = jwtProvider.createAccessToken(userId);
            RefreshTokenInfo rtInfo = jwtProvider.createRefreshToken(userId);

            jwtStorageService.save(atInfo, rtInfo);

            //when then
            ResponseCode responseCode = ResponseCode.SUCCESS;
            mvc.perform(post(path)
                            .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, atInfo.getJwt())))
                    .andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()))
                    .andExpectAll(
                            cookie().exists(ACCESS_TOKEN_COOKIE_NAME),
                            cookie().path(ACCESS_TOKEN_COOKIE_NAME, "/"),
                            cookie().maxAge(ACCESS_TOKEN_COOKIE_NAME, 0))
                    .andExpectAll(
                            cookie().exists(REFRESH_TOKEN_COOKIE_NAME),
                            cookie().path(REFRESH_TOKEN_COOKIE_NAME, "/api/auth/refresh"),
                            cookie().maxAge(REFRESH_TOKEN_COOKIE_NAME, 0));

            verify(jwtStorageService, times(1)).delete(any());
        }

        @Test
        @DisplayName("만료된 AT 테스트")
        @Transactional
        void shouldOk_WhenExpiredAt() throws Exception {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            Long userId = user.getId();
            Date expiredDate = getExpiredDate();
            AccessTokenInfo atInfo = jwtProvider.createAccessToken(userId, expiredDate, expiredDate);
            RefreshTokenInfo rtInfo = jwtProvider.createRefreshToken(userId);

            jwtStorageService.save(atInfo, rtInfo);

            //when then
            ResponseCode responseCode = ResponseCode.SUCCESS;
            mvc.perform(post(path)
                            .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, atInfo.getJwt())))
                    .andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()))
                    .andExpectAll(
                            cookie().exists(ACCESS_TOKEN_COOKIE_NAME),
                            cookie().path(ACCESS_TOKEN_COOKIE_NAME, "/"),
                            cookie().maxAge(ACCESS_TOKEN_COOKIE_NAME, 0))
                    .andExpectAll(
                            cookie().exists(REFRESH_TOKEN_COOKIE_NAME),
                            cookie().path(REFRESH_TOKEN_COOKIE_NAME, "/api/auth/refresh"),
                            cookie().maxAge(REFRESH_TOKEN_COOKIE_NAME, 0));

            verify(jwtStorageService, times(1)).delete(any());
        }


        @Test
        @DisplayName("AT가 없을 때 테스트")
        void should401_WhenNoAt() throws Exception {
            //when then
            ResponseCode responseCode = ResponseCode.FORBIDDEN;
            mvc.perform(post(path))
                    .andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()));
        }
    }

    @Nested
    @DisplayName("인증 토큰 재발급 테스트")
    class RefreshTokenTest {

        private static final String path = "/api/auth/refresh";

        @Test
        @DisplayName("정상 AT, 정상 RT => 403 FORBIDDEN")
        @Transactional
        void shouldRefresh_WhenNormalATNormalRT() throws Exception {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            Long userId = user.getId();
            AccessTokenInfo atInfo = jwtProvider.createAccessToken(userId);
            RefreshTokenInfo rtInfo = jwtProvider.createRefreshToken(userId);

            int rtExpireSecond = REFRESH_TOKEN_EXPIRE_MINUTE * 60;
            int atExpireSecond = rtExpireSecond;

            jwtStorageService.save(atInfo, rtInfo);

            //when then
            ResponseCode responseCode = ResponseCode.FORBIDDEN;
            mvc.perform(post(path)
                            .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, atInfo.getJwt()))
                            .cookie(new Cookie(REFRESH_TOKEN_COOKIE_NAME, rtInfo.getJwt())))
                    .andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()));
        }

        @Test
        @DisplayName("만료된 AT, 정상 RT => 200 SUCCESS")
        @Transactional
        void shouldRefresh_WhenExpiredATNormalRT() throws Exception {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            Long userId = user.getId();
            Date expiredDate = getExpiredDate();

            AccessTokenInfo atInfo = jwtProvider.createAccessToken(userId, expiredDate, expiredDate);
            RefreshTokenInfo rtInfo = jwtProvider.createRefreshToken(userId);

            int rtExpireSecond = REFRESH_TOKEN_EXPIRE_MINUTE * 60;
            //AT는 재발급 시 검증을 위해 RT 만료 전까지 쿠키에 살아있어야 한다.
            int atExpireSecond = rtExpireSecond;

            jwtStorageService.save(atInfo, rtInfo);

            //when then
            ResponseCode responseCode = ResponseCode.SUCCESS;
            mvc.perform(post(path)
                            .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, atInfo.getJwt()))
                            .cookie(new Cookie(REFRESH_TOKEN_COOKIE_NAME, rtInfo.getJwt())))
                    .andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()))
                    .andExpectAll(
                            cookie().exists(ACCESS_TOKEN_COOKIE_NAME),
                            cookie().path(ACCESS_TOKEN_COOKIE_NAME, "/"),
                            cookie().maxAge(ACCESS_TOKEN_COOKIE_NAME, atExpireSecond))
                    .andExpectAll(
                            cookie().exists(REFRESH_TOKEN_COOKIE_NAME),
                            cookie().path(REFRESH_TOKEN_COOKIE_NAME, "/api/auth/refresh"),
                            cookie().maxAge(REFRESH_TOKEN_COOKIE_NAME, rtExpireSecond));

        }

        @Test
        @DisplayName("정상 AT, 만료된 RT => 403 FORBIDDEN")
        @Transactional
        void shouldNotRefresh_WhenNormalATExpiredRT() throws Exception {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            Long userId = user.getId();
            Date expiredDate = getExpiredDate();

            AccessTokenInfo atInfo = jwtProvider.createAccessToken(userId);
            RefreshTokenInfo rtInfo = jwtProvider.createRefreshToken(userId, expiredDate, expiredDate);

            //when then
            ResponseCode responseCode = ResponseCode.FORBIDDEN;
            mvc.perform(post(path)
                            .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, atInfo.getJwt()))
                            .cookie(new Cookie(REFRESH_TOKEN_COOKIE_NAME, rtInfo.getJwt())))
                    .andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()));
        }


        @Test
        @DisplayName("만료된 AT, 만료된 RT => 401 AUTH_EXPIRED_REFRESH_TOKEN")
        @Transactional
        void shouldNotRefresh_WhenExpiredATExpiredRT() throws Exception {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            Long userId = user.getId();
            Date expiredDate = getExpiredDate();

            AccessTokenInfo atInfo = jwtProvider.createAccessToken(userId, expiredDate, expiredDate);
            RefreshTokenInfo rtInfo = jwtProvider.createRefreshToken(userId, expiredDate, expiredDate);

            //when
            ResponseCode responseCode = ResponseCode.AUTH_EXPIRED_REFRESH_TOKEN;
            ResultActions perform = mvc.perform(post(path)
                    .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, atInfo.getJwt()))
                    .cookie(new Cookie(REFRESH_TOKEN_COOKIE_NAME, rtInfo.getJwt())));

            //then
            perform.andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()));
        }

        @Test
        @DisplayName("만료된 AT, 사용한 RT => 403 FORBIDDEN")
        @Transactional
        void shouldNotRefresh_WhenExpiredATUsedRT() throws Exception {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            Long userId = user.getId();
            Date expiredDate = getExpiredDate();

            AccessTokenInfo atInfo = jwtProvider.createAccessToken(userId, expiredDate, expiredDate);
            RefreshTokenInfo rtInfo = jwtProvider.createRefreshToken(userId);

            //등록 후 삭제 = 필요없는 코드이지만 의도 파악을 위해 남겨둠
            jwtStorageService.save(atInfo, rtInfo);
            jwtStorageService.delete(atInfo);

            //when then
            ResponseCode responseCode = ResponseCode.FORBIDDEN;
            mvc.perform(post(path)
                            .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, atInfo.getJwt()))
                            .cookie(new Cookie(REFRESH_TOKEN_COOKIE_NAME, rtInfo.getJwt())))
                    .andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()));
        }

        @Test
        @DisplayName("정지된 유저일 경우 => 403 AUTH_SUSPENDED_USER")
        @Transactional
        void should403_WhenSuspendedUser() throws Exception {
            //given
            //정지할 유저 생성
            UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
            User user = userJoinService.registerUser(userRegisterRequest);

            //관리자 유저 생성
            UserRegisterRequest adminRegisterRequest = UserSample.getAdminUserRegisterRequest();
            User admin = userJoinService.registerUser(adminRegisterRequest);

            //정지 요청 생성
            LocalDateTime endDate = LocalDateTime.now().plusDays(1).withNano(0);
            String reason = "test";
            SuspendUserRequest suspendUserRequest = new SuspendUserRequest(reason, endDate);
            //정지
            userSuspensionService.suspendUser(admin.getId(), user.getId(), suspendUserRequest);

            //토큰 생성
            Date expiredDate = getExpiredDate();
            AccessTokenInfo atInfo = jwtProvider.createAccessToken(user.getId(), expiredDate, expiredDate);
            RefreshTokenInfo rtInfo = jwtProvider.createRefreshToken(user.getId());
            jwtStorageService.save(atInfo, rtInfo);

            //when
            ResultActions resultActions = mvc.perform(post(path)
                    .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, atInfo.getJwt()))
                    .cookie(new Cookie(REFRESH_TOKEN_COOKIE_NAME, rtInfo.getJwt()))
            );

            //then
            ResponseCode responseCode = ResponseCode.AUTH_SUSPENDED_USER;
            resultActions.andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()))
                    .andExpect(jsonPath("$.data.endDate").value(endDate.toString()))
                    .andExpect(jsonPath("$.data.reason").value(reason));
        }
    }

    private Date getExpiredDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, -10);
        return calendar.getTime();
    }
}
