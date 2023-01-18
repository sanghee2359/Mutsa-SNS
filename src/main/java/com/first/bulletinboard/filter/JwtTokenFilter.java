package com.first.bulletinboard.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.bulletinboard.domain.ErrorResponse;
import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    /**
     * request 에서 전달받은 Jwt 토큰을 확인
     *
     */

    private final String BEARER = "Bearer ";

    private final JwtTokenUtil jwtTokenUtil;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        request.setAttribute("existsToken", true); // 토큰 존재 여부 초기화
        if (isEmptyToken(token)) {
            request.setAttribute("existsToken", false); // 토큰이 없는 경우 false로 변경
        }

        // token null이거나 Bearer type 여부 확인
        if (token == null || !token.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("authentication:{}",token);
        token = parseBearer(token);
        log.info("token(filter):{}",token);

        // 유효성 체크
        if (jwtTokenUtil.validateToken(token,secretKey)) {
            Authentication authentication = jwtTokenUtil.getAuthentication(token,secretKey);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
    private boolean isEmptyToken(String token) {
        return token == null || "".equals(token);
    }

    private String parseBearer(String token) {
        return token.substring(BEARER.length());
    }


    /**
     * Security Chain 에서 발생하는 에러 응답 구성
     */
    public static void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        ObjectMapper objectMapper = new ObjectMapper();

        ErrorResponse errorResponse = new ErrorResponse
                (errorCode, errorCode.getMessage());

        Response<ErrorResponse> error = Response.error(errorResponse);
        String s = objectMapper.writeValueAsString(error);

        /**
         * 한글 출력을 위해 getWriter() 사용
         */
        response.getWriter().write(s);
    }

}
