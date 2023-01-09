package com.first.bulletinboard.controller;

import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.domain.dto.user.*;
import com.first.bulletinboard.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

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
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest userLoginRequest) {
        String token = userService.login(userLoginRequest.getUserName(), userLoginRequest.getPassword());
        return Response.success(new UserLoginResponse(token));
    }

    @ApiOperation(value = "user 권한 변경", notes = "ADMIN만 접근 가능합니다")
    @PostMapping("/{id}/role/change")
    public Response<UserRoleChangeResponse> roleChange(@PathVariable int id) {
        UserDto dto = userService.roleChange(id);
        return Response.success(new UserRoleChangeResponse(dto.getId()
                ,String.format("%s 으로 권한 변경 완료", dto.getRole())));
    }

    @ApiOperation(value = "user 조회", notes = "ADMIN만 접근 가능합니다")
    @GetMapping
    public Response<Page<UserReadResponse>> findUserList(@PageableDefault(size = 20, sort = "createdAt") Pageable pageable){
        Page<UserDto> users = userService.findAllUser(pageable);
        return Response.success(users.map(UserReadResponse::of));
    }


}
