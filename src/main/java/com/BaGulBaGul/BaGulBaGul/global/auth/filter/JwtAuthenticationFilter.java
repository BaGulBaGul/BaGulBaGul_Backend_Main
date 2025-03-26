package com.BaGulBaGul.BaGulBaGul.global.auth.filter;

import com.BaGulBaGul.BaGulBaGul.global.auth.exception.AccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.RefreshTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtCookieService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtCookieService jwtCookieService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        authenticate(request, response);
        filterChain.doFilter(request, response);
    }

    /*
     * Cookie에 있는 Access Token을 검증 후 userId를 추출해서 인증 처리
     * 만약 AccessToken이 만료되었다면 RefreshToken을 검증 후 토큰을 재발급
     * 만약 RefreshToken도 만료되었다면 인증 실패
     */
    private void authenticate(HttpServletRequest request, HttpServletResponse response) {
        //AccessToken 추출
        String accessToken = jwtCookieService.getAccessToken(request);

        //AccessToken 검증 후 userId 추출 시도
        Long userId;
        try {
            userId = jwtProvider.getUserIdFromAccessToken(accessToken);
        }
        //userId가 없다면 RefreshToken 검증 후 AccessToken, RefreshToken 재발급 시도
        catch (AccessTokenException ae) {
            String refreshToken = jwtCookieService.getRefreshToken(request);
            //RefreshToken 검증 후 userId 추출 시도
            try {
                userId = jwtProvider.getUserIdFromRefreshToken(refreshToken);
            }
            //RefreshToken도 없다면 인증 실패
            catch (RefreshTokenException re) {
                return;
            }

            //검증 성공, 토큰 재발급
            accessToken = jwtProvider.createAccessToken(userId);
            refreshToken = jwtProvider.createRefreshToken(userId);
            //재발급한 토큰을 응답 쿠키에 저장
            jwtCookieService.setAccessToken(response, accessToken);
            jwtCookieService.setRefreshToken(response, refreshToken);
        }

        //인증 처리를 위해 security context설정
        AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userId,
                null,
                AuthorityUtils.NO_AUTHORITIES
        );
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
    }
}
