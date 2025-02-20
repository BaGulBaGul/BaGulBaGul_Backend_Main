package com.BaGulBaGul.BaGulBaGul.global.auth.oauth2;

import com.BaGulBaGul.BaGulBaGul.domain.user.SocialLoginUser;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.dto.ApplicationOAuth2User;
import com.BaGulBaGul.BaGulBaGul.global.auth.oauth2.dto.OAuth2JoinTokenSubject;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.SocialLoginUserRepository;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtCookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final SocialLoginUserRepository socialUserRepository;
    private final JwtProvider jwtProvider;
    private final JwtCookieService jwtCookieService;

    @Value("${spring.security.oauth2.client.front_join_redirect_url}")
    private String FRONT_JOIN_REDIRECT_URL;

    @Value("${spring.security.oauth2.client.front_login_redirect_url}")
    private String FRONT_LOGIN_REDIRECT_URL;

    private final String JOIN_TOKEN_PARAM_NAME = "join_token";



    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException{
        ApplicationOAuth2User applicationOAuth2User = (ApplicationOAuth2User) authentication.getPrincipal();
        //소셜 로그인 유저 검색
        String socialLoginId = applicationOAuth2User.getSocialLoginId();
        SocialLoginUser socialLoginUser = socialUserRepository.findWithUserById(socialLoginId).orElse(null);
        //회원가입 필요한 경우 oauth join token과 함께 회원가입 추가정보 페이지로 redirect
        if(socialLoginUser == null) {
            OAuth2JoinTokenSubject oAuth2JoinTokenSubject = OAuth2JoinTokenSubject.builder()
                    .socialLoginId(socialLoginId)
                    .oAuth2Provider(applicationOAuth2User.getOAuthProvider())
                    .build();
            String oauth2JoinToken = jwtProvider.createOAuth2JoinToken(oAuth2JoinTokenSubject);
            response.sendRedirect(
                    composeQueryParam(FRONT_JOIN_REDIRECT_URL, JOIN_TOKEN_PARAM_NAME, oauth2JoinToken)
            );
        }
        //이미 가입된 유저라면 access, refresh 토큰을 쿠키에 저장하고 프론트의 로그인 성공 페이지로 리다이렉트
        else{
            Long userId = socialLoginUser.getUser().getId();
            //토큰 발급
            String accessToken = jwtProvider.createAccessToken(userId);
            String refreshToken = jwtProvider.createRefreshToken(userId);
            //쿠키 저장
            jwtCookieService.setAccessToken(response, accessToken);
            jwtCookieService.setRefreshToken(response, refreshToken);
            //로그인 성공 처리 페이지로 리다이렉트
            response.sendRedirect(
                    FRONT_LOGIN_REDIRECT_URL
            );
        }
    }

    private String composeQueryParam(String url, String paramName, String value) {
        return url + "?" + paramName + "=" + value;
    }
}
