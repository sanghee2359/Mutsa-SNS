package com.first.bulletinboard.config.securityErrorHandling;

import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.filter.JwtTokenFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        log.info("exception! authorization:{}",authorization);
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("토큰이 존재하지 않거나 Bearer로 시작하지 않는 경우");
            ErrorCode errorCode = ErrorCode.INVALID_TOKEN;
            JwtTokenFilter.setErrorResponse(response, errorCode);
        } else if (authorization.equals(ErrorCode.EXPIRED_TOKEN)) {
            log.error("토큰이 만료된 경우");
            ErrorCode errorCode = ErrorCode.EXPIRED_TOKEN;
            JwtTokenFilter.setErrorResponse(response,errorCode);
        }
    }
}

