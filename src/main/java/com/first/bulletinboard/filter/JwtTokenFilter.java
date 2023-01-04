package com.first.bulletinboard.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.bulletinboard.domain.ErrorResponse;
import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.service.UserService;
import com.first.bulletinboard.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorization == null || !authorization.startsWith("Bearer ")) {
            log.info("authorization을 잘못 보냈습니다.");
            filterChain.doFilter(request, response);
            return;
        }
        // userName에서 token꺼내기
        String token = authorization.split(" ")[1];
        if(JwtTokenUtil.isExpired(token, secretKey)) {
            log.info("token이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // username 꺼내기
        String userName = JwtTokenUtil.getUserName(token, secretKey);
        log.info("userName:{}", userName);

        //Role 가져오기
        UserDetails userDetails = userService.loadUserByUsername(userName);

        // authentication 구현체(객체 생성)
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

        // detail 추가
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    /**
     * Security Chain 에서 발생하는 에러 응답 구성
     */
    public static void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorCode.getHttpStatus().value());
        ObjectMapper objectMapper = new ObjectMapper();
// 여기 바꿨음
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
