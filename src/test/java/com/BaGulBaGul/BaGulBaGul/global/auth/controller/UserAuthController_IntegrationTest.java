package com.BaGulBaGul.BaGulBaGul.global.auth.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.Cookie;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
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

    @Value("${user.login.access_token_cookie_name}")
    private String ACCESS_TOKEN_COOKIE_NAME;

    @Value("${user.login.refresh_token_cookie_name}")
    private String REFRESH_TOKEN_COOKIE_NAME;

    @Value("${user.login.refresh_token_expire_minute}")
    private int REFRESH_TOKEN_EXPIRE_MINUTE;

    @BeforeEach
    void setup(WebApplicationContext ctx) {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .apply(springSecurity())
                .defaultRequest(get("/").with(csrf()))
                .build();
    }

    @Nested
    @DisplayName("인증 토큰 재발급 테스트")
    class RefreshTokenTest {
        @Test
        @DisplayName("정상 AT, 정상 RT => 403 FORBIDDEN")
        void shouldRefresh_WhenNormalATNormalRT() throws Exception {
            //given
            Long userId = 1L;
            String accessToken = jwtProvider.createAccessToken(userId).getJwt();
            String refreshToken = jwtProvider.createRefreshToken(userId).getJwt();

            int rtExpireSecond = REFRESH_TOKEN_EXPIRE_MINUTE * 60;
            int atExpireSecond = rtExpireSecond;

            //when then
            ResponseCode responseCode = ResponseCode.FORBIDDEN;
            mvc.perform(post("/api/auth/refresh")
                            .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken))
                            .cookie(new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken)))
                    .andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()));
        }

        @Test
        @DisplayName("만료된 AT, 정상 RT => 200 SUCCESS")
        void shouldRefresh_WhenExpiredATNormalRT() throws Exception {
            //given
            Long userId = 1L;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, -10);
            Date expiredDate = calendar.getTime();

            String accessToken = jwtProvider.createAccessToken(userId, expiredDate, expiredDate).getJwt();
            String refreshToken = jwtProvider.createRefreshToken(userId).getJwt();

            int rtExpireSecond = REFRESH_TOKEN_EXPIRE_MINUTE * 60;
            int atExpireSecond = rtExpireSecond;

            //when then
            ResponseCode responseCode = ResponseCode.SUCCESS;
            mvc.perform(post("/api/auth/refresh")
                            .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken))
                            .cookie(new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken)))
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
        @DisplayName("정상 AT, 만료된 RT =? 403 FORBIDDEN")
        void shouldNotRefresh_WhenNormalATExpiredRT() throws Exception {
            //given
            Long userId = 1L;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, -10);
            Date expiredDate = calendar.getTime();

            String accessToken = jwtProvider.createAccessToken(userId).getJwt();
            String refreshToken = jwtProvider.createRefreshToken(userId, expiredDate, expiredDate).getJwt();

            //when then
            ResponseCode responseCode = ResponseCode.FORBIDDEN;
            mvc.perform(post("/api/auth/refresh")
                            .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken))
                            .cookie(new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken)))
                    .andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()));
        }


        @Test
        @DisplayName("만료된 AT, 만료된 RT => 401 AUTH_EXPIRED_REFRESH_TOKEN")
        void shouldNotRefresh_WhenExpiredATExpiredRT() throws Exception {
            //given
            Long userId = 1L;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MINUTE, -10);
            Date expiredDate = calendar.getTime();

            String accessToken = jwtProvider.createAccessToken(userId, expiredDate, expiredDate).getJwt()
            String refreshToken = jwtProvider.createRefreshToken(userId, expiredDate, expiredDate).getJwt();

            //when
            ResponseCode responseCode = ResponseCode.AUTH_EXPIRED_REFRESH_TOKEN;
            ResultActions perform = mvc.perform(post("/api/auth/refresh")
                    .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken))
                    .cookie(new Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken)));

            //then
            perform.andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()));
        }
    }
}