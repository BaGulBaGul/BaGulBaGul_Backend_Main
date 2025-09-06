package com.BaGulBaGul.BaGulBaGul.global.auth.oauth2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.SuspendUserRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.SocialLoginUserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.SocialLoginUserService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserSuspensionService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.constant.OAuth2Provider;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.dto.ApplicationOAuth2User;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.dto.OAuth2JoinTokenSubject;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class OAuth2SuccessHandler_IntegrationTest {

    @Autowired
    OAuth2SuccessHandler oAuth2SuccessHandler;
    @Autowired
    UserJoinService userJoinService;
    @Autowired
    UserSuspensionService userSuspensionService;
    @Autowired
    SocialLoginUserService socialLoginUserService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    EntityManager em;

    @Autowired
    SocialLoginUserRepository socialLoginUserRepository;

    @Value("${spring.security.oauth2.client.front_suspended_user_redirect_url}")
    private String FRONT_SUSPENDED_USER_REDIRECT_URL;
    @Value("${spring.security.oauth2.client.front_join_redirect_url}")
    private String FRONT_JOIN_REDIRECT_URL;
    @Value("${spring.security.oauth2.client.front_login_redirect_url}")
    private String FRONT_LOGIN_REDIRECT_URL;

    // ApplicationOAuth2User의 protected 생성자에 접근하기 위한 테스트용 subclass
    static class TestApplicationOAuth2User extends ApplicationOAuth2User {
        public TestApplicationOAuth2User(String oauthId, OAuth2Provider oAuthProvider) {
            super(oauthId, oAuthProvider, null);
        }
    }

    @Test
    @DisplayName("신규 유저가 OAuth2로 로그인 시 회원가입 페이지로 리다이렉트되어야 한다.")
    @Transactional
    void test_onAuthenticationSuccess_ifNewUser() throws Exception {
        //given
        ApplicationOAuth2User oAuth2User = new TestApplicationOAuth2User("new_user_social_id", OAuth2Provider.kakao);
        Authentication authentication = new UsernamePasswordAuthenticationToken(oAuth2User, null,
                oAuth2User.getAuthorities());

        // 소셜 로그인 유저가 없는 상태

        // Mock 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        //when
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);

        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(response).sendRedirect(captor.capture());
        String redirectUrl = captor.getValue();

        assertThat(redirectUrl).startsWith(FRONT_JOIN_REDIRECT_URL);

        URI uri = new URI(redirectUrl);
        String query = uri.getQuery();
        assertThat(query).isNotNull();

        Map<String, String> queryParams = Arrays.stream(query.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(a -> a[0], a -> a.length > 1 ? a[1] : ""));

        assertThat(queryParams).containsKey("join_token");
        assertThat(queryParams.get("join_token")).isNotBlank();
    }

    @Test
    @DisplayName("기존 유저가 OAuth2로 로그인 시 로그인 성공 페이지로 리다이렉트되어야 한다.")
    @Transactional
    void test_onAuthenticationSuccess_ifExistingUser() throws Exception {
        //given
        ApplicationOAuth2User oAuth2User = new TestApplicationOAuth2User("existing_user_social_id", OAuth2Provider.kakao);
        Authentication authentication = new UsernamePasswordAuthenticationToken(oAuth2User, null,
                oAuth2User.getAuthorities());
        // 소셜 로그인 유저 생성
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        String socialLoginId = oAuth2User.getSocialLoginId();
        OAuth2JoinTokenSubject oAuth2JoinTokenSubject = OAuth2JoinTokenSubject.builder()
                .socialLoginId(socialLoginId)
                .oAuth2Provider(oAuth2User.getOAuthProvider())
                .build();
        String joinToken = jwtProvider.createOAuth2JoinToken(oAuth2JoinTokenSubject).getJwt();
        socialLoginUserService.registerSocialLoginUser(user, joinToken);

        em.flush();
        em.clear();

        // Mock 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        //when
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);

        //then
        verify(response).sendRedirect(FRONT_LOGIN_REDIRECT_URL);
    }

    @Test
    @DisplayName("정지된 유저가 OAuth2로 로그인 시 정지 페이지로 리다이렉트되어야 한다.")
    @Transactional
    void test_onAuthenticationSuccess_ifUserSuspended() throws Exception {
        //given
        ApplicationOAuth2User oAuth2User = new TestApplicationOAuth2User("test_social_id", OAuth2Provider.kakao);
        Authentication authentication = new UsernamePasswordAuthenticationToken(oAuth2User, null,
                oAuth2User.getAuthorities());

        // 소셜 로그인 유저 생성
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        String socialLoginId = oAuth2User.getSocialLoginId();
        OAuth2JoinTokenSubject oAuth2JoinTokenSubject = OAuth2JoinTokenSubject.builder()
                .socialLoginId(socialLoginId)
                .oAuth2Provider(oAuth2User.getOAuthProvider())
                .build();
        String joinToken = jwtProvider.createOAuth2JoinToken(oAuth2JoinTokenSubject).getJwt();
        socialLoginUserService.registerSocialLoginUser(user, joinToken);

        // 유저 정지
        User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        String reason = "test reason";
        LocalDateTime endDate = LocalDateTime.now().plusDays(7).withNano(0);
        userSuspensionService.suspendUser(admin.getId(), user.getId(), new SuspendUserRequest(reason, endDate));

        em.flush();
        em.clear();

        // Mock 객체 생성
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        //when
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);

        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(response).sendRedirect(captor.capture());
        String redirectUrl = captor.getValue();

        assertThat(redirectUrl).startsWith(FRONT_SUSPENDED_USER_REDIRECT_URL);

        URI uri = new URI(redirectUrl);
        String query = uri.getQuery();
        assertThat(query).isNotNull();

        Map<String, String> queryParams = Arrays.stream(query.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(a -> a[0], a -> a.length > 1 ? URLDecoder.decode(a[1], StandardCharsets.UTF_8) : ""));

        assertThat(queryParams).containsKey("endDate");
        assertThat(queryParams.get("endDate")).isEqualTo(endDate.toString());
        assertThat(queryParams).containsKey("reason");
        assertThat(queryParams.get("reason")).isEqualTo(reason);
    }
}