package com.BaGulBaGul.BaGulBaGul.global.auth.filter;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JwtAuthenticationFilter_IntegrationTest {

    public static final String SECURE_PATH = "/api/user/info/my";
    public static final String NORMAL_PATH = "/";

    @Autowired
    MockMvc mvc;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserJoinService userJoinService;

    @Value("${user.login.access_token_cookie_name}")
    private String ACCESS_TOKEN_COOKIE_NAME;

    @BeforeEach
    void setup(WebApplicationContext ctx) {
        mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("인증이 필요 없는 api에 access token 없이 요청 => 200")
    @Transactional
    void shouldOk_WhenNotNeedAuthenticate_WhenNotOfferAT() throws Exception {
        //given
        Long userId = 1L;
        String accessToken = jwtProvider.createAccessToken(userId).getJwt();

        mvc.perform(get(NORMAL_PATH)
                        .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인증이 필요 없는 api에 정상 access token => 200")
    @Transactional
    void shouldOk_WhenNotNeedAuthenticate_WhenOfferNormalAT() throws Exception {
        mvc.perform(get(NORMAL_PATH))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인증이 필요한 api에 정상 access token => 200")
    @Transactional
    void shouldOk_WhenNeedAuthenticate_WhenOfferNormalAT() throws Exception {
        //given
        User user = userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST);
        Long userId = user.getId();
        String accessToken = jwtProvider.createAccessToken(userId).getJwt();

        mvc.perform(get(SECURE_PATH)
                .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errorCode").value(ResponseCode.SUCCESS.getCode()));
    }

    @Test
    @DisplayName("인증이 필요한 api에 만료된 access token => 401 AUTH_EXPIRED_ACCESS_TOKEN")
    @Transactional
    void shouldResponseAUTH_EXPIRED_ACCESS_TOKEN_WhenNeedAuthenticate_WhenOfferTamperedAT() throws Exception {
        //given
        User user = userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST);
        Long userId = user.getId();
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, -20);
        Date issuedAt = calendar.getTime();
        calendar.add(Calendar.MINUTE, 15);
        Date expireAt = calendar.getTime();
        String accessToken = jwtProvider.createAccessToken(userId, issuedAt, expireAt).getJwt();

        //when
        mvc.perform(get(SECURE_PATH)
                        .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(ResponseCode.AUTH_EXPIRED_ACCESS_TOKEN.getCode()));
    }

    @Test
    @DisplayName("인증이 필요한 api에 변조된 access token => 401 UNAUTHORIZED")
    @Transactional
    void shouldResponseUNAUTHORIZED_WhenNeedAuthenticate_WhenOfferTamperedAT() throws Exception {
        //given
        User user = userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST);
        Long userId = user.getId();
        String accessToken = jwtProvider.createAccessToken(userId).getJwt();
        //서명 마지막 문자만 변조
        String b64urlChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                "abcdefghijklmnopqrstuvwxyz" +
                "0123456789" +
                "-_";
        char last = accessToken.charAt(accessToken.length() - 1);
        char newLast = b64urlChars
                .chars()
                .mapToObj(c -> (char) c)
                .filter(c -> c != last)    // 기존 문자와 다르게
                .findFirst()
                .orElseThrow();
        accessToken = accessToken.substring(0, accessToken.length() - 1) + newLast;

        //when
        mvc.perform(get(SECURE_PATH)
                        .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, accessToken)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errorCode").value(ResponseCode.UNAUTHORIZED.getCode()));
    }
}