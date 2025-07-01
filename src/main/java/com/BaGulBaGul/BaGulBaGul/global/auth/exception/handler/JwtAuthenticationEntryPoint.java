package com.BaGulBaGul.BaGulBaGul.global.auth.exception.handler;

import com.BaGulBaGul.BaGulBaGul.global.auth.exception.ExpiredAccessTokenException;
import com.BaGulBaGul.BaGulBaGul.global.auth.filter.JwtAuthenticationFilter;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        //AT 만료
        Boolean isATExpired = (Boolean) request.getAttribute(JwtAuthenticationFilter.ACCESS_TOKEN_EXPIRE_MARK);
        if(isATExpired != null && isATExpired) {
            ResponseCode responseCode = ResponseCode.AUTH_EXPIRED_ACCESS_TOKEN;
            response.setStatus(responseCode.getHttpStatus().value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    objectMapper.writeValueAsString(ApiResponse.of(null, responseCode))
            );
        }
        //그 외의 모든 인증 실패
        else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    objectMapper.writeValueAsString(ApiResponse.of(null, ResponseCode.UNAUTHORIZED))
            );
        }
    }
}