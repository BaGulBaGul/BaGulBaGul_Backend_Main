package com.BaGulBaGul.BaGulBaGul.global.config;

import com.BaGulBaGul.BaGulBaGul.domain.user.auth.filter.JwtAuthenticationFilter;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.oauth2.OAuth2SuccessHandler;
import com.BaGulBaGul.BaGulBaGul.domain.user.auth.oauth2.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final OAuth2UserService oAuth2UserService;
    private final OAuth2SuccessHandler oAuthSuccessHandler;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .httpBasic().disable()
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션x
                .authorizeRequests()
                //event 비로그인 허용 경로
                .antMatchers(HttpMethod.GET, "/api/event").permitAll()
                .regexMatchers(HttpMethod.GET, "/api/event/\\d+").permitAll()

                //post 비로그인 허용 경로
                .regexMatchers(HttpMethod.GET, "/api/post/\\d+/comment").permitAll()
                .regexMatchers(HttpMethod.GET, "/api/post/comment/\\d+").permitAll()
                .regexMatchers(HttpMethod.GET, "/api/post/comment/\\d+/children").permitAll()

                //recruitment 비로그인 혀용 경로
                .regexMatchers(HttpMethod.GET, "/api/event/\\d+/recruitment").permitAll()
                .regexMatchers(HttpMethod.GET, "/api/event/recruitment/\\d+").permitAll()

                //user 비로그인 허용 경로
                .regexMatchers(HttpMethod.GET, "/api/user/info/\\d+").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/join/social").permitAll()

                //나머지 로그인 필요
                .anyRequest().authenticated();

                // JWT 로그인 필터
                http.addFilterAfter(jwtAuthenticationFilter, CorsFilter.class);
                // 로그인 예외 처리 EntryPoint
                http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
                // OAuth2 서비스, 헨들러 등록
                http.oauth2Login().userInfoEndpoint().userService(oAuth2UserService).and().successHandler(oAuthSuccessHandler);

        return http.build();
    }

    /* 스프링 시큐리티 룰을 무시하게 하는 Url 규칙(여기 등록하면 규칙 적용하지 않음) */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                // 스웨거 경로
                // 인증이 필요하지 않는 경로 추가
                .antMatchers("/doc", "/swagger*/**", "/favicon*/**", "/v2/api-docs");
    }
}
