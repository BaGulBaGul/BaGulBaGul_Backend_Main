package com.BaGulBaGul.BaGulBaGul.global.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    @Getter
    @Setter
    @AllArgsConstructor
    private static class AuthEntry {
        private HttpMethod httpMethod;
        private String path;
    }

    /*
        인증이 필요한 모든 경로를 적어주기
        ex)new AuthEntry(HttpMethod.POST, "/api/post")
     */
    private static final AuthEntry[] needAuth = {
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //needAuth에 있는 모든 method/path는 인증 필요
        for(AuthEntry authEntry : needAuth) {
            http.authorizeRequests().antMatchers(authEntry.httpMethod, authEntry.path).authenticated();
        }
        //그 외는 전부 허용
        http.authorizeRequests().anyRequest().permitAll();
    }
}
