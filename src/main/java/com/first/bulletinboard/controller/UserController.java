package com.first.bulletinboard.controller;

import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.domain.dto.token.TokenResponse;
import com.first.bulletinboard.domain.dto.user.*;
import com.first.bulletinboard.service.UserService;
import com.first.bulletinboard.utils.JwtTokenUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    // 회원가입
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest userJoinRequest) {
        UserDto userDto = userService.join(userJoinRequest);
        log.info("userResponse:{}", new UserJoinResponse(userDto.getId(), userDto.getUserName()));
        return Response.success(new UserJoinResponse(userDto.getId(), userDto.getUserName()));
    }
    // 로그인
    @PostMapping("/login")
    public Response<TokenResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        TokenResponse tokens = userService.login(userLoginRequest.getUserName(), userLoginRequest.getPassword());
        return Response.success(tokens);
    }

    @ApiOperation(value = "user 권한 변경", notes = "ADMIN만 접근 가능합니다")
    @PostMapping("/{userId}/role/change")
    public Response<UserRoleChangeResponse> roleChange(@PathVariable Long userId) {
        UserDto dto = userService.roleChange(userId);
        return Response.success(new UserRoleChangeResponse(dto.getId()
                ,String.format("%s 으로 권한 변경 완료", dto.getRole())));
    }

    @ApiOperation(value = "user 조회", notes = "ADMIN만 접근 가능합니다")
    @GetMapping
    public Response<Page<UserReadResponse>> findUserList(@PageableDefault(size = 20, sort = "createdAt") Pageable pageable){
        log.info("find users in controller ");

        Page<UserDto> users = userService.findAllUser(pageable);
        return Response.success(users.map(UserReadResponse::of));
    }
    /*@GetMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            try{

            }
    }*/


}
