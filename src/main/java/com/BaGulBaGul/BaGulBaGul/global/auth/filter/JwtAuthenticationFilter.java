package com.BaGulBaGul.BaGulBaGul.global.auth.filter;

import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AccessTokenInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.ExpiredAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.exception.InvalidAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtCookieService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.PermissionService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final JwtCookieService jwtCookieService;
    private final PermissionService permissionService;


    public static final String ACCESS_TOKEN_EXPIRE_MARK = "AT_EXPIRED";
    public static final String ACCESS_TOKEN_INVALID_MARK = "AT_INVALID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        authenticate(request, response);
        filterChain.doFilter(request, response);
    }

    /*
     * Cookie에 있는 AT(AccessToken)를 검증 후 SecurityContext에 유저 정보를 넣는다.
     * AT가 만료되었다면 ExpiredAccessTokenException
     * 잘못된 토큰이라면 InvalidAccessTokenException
     */
    private void authenticate(HttpServletRequest request, HttpServletResponse response)
        throws ExpiredAccessTokenException, InvalidAccessTokenException {

        //AccessToken 추출
        String accessToken = jwtCookieService.getAccessToken(request);
        if(accessToken == null) {
            return;
        }

        //AccessToken 검증 후 정보를 파싱
        AccessTokenInfo parsedAccessTokenInfo;
        try {
            parsedAccessTokenInfo = jwtProvider.parseAccessToken(accessToken);
        } catch (ExpiredAccessTokenException e) {
            request.setAttribute(ACCESS_TOKEN_EXPIRE_MARK, true);
            return;
        } catch (InvalidAccessTokenException e) {
            request.setAttribute(ACCESS_TOKEN_INVALID_MARK, true);
            return;
        }

        Long userId = parsedAccessTokenInfo.getUserId();
        List<String> roles = parsedAccessTokenInfo.getRoles();

        //역할과 권한을 모두 GrantedAuthority로 변환
        List<GrantedAuthority> authorities = new ArrayList<>();
        //역할 추가
        authorities.addAll(roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList()));
        //권한 추가
        authorities.addAll(
                permissionService.getPermissionsByRoles(roles).stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.name()))
                        .collect(Collectors.toList())
        );

        AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                .userId(userId)
                .roles(roles)
                .build();
		
        AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticatedUserInfo,
                null,
                authorities
        );
		
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
    }
}