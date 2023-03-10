package com.first.bulletinboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.bulletinboard.domain.dto.user.UserDto;
import com.first.bulletinboard.domain.dto.user.UserJoinRequest;
import com.first.bulletinboard.domain.dto.user.UserLoginRequest;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService; // 가짜 service 객체

    @Autowired
    ObjectMapper objectMapper; // jackson 객체 : 자바 object를 json으로 만들어준다

    UserJoinRequest userJoinRequest = UserJoinRequest.builder()
            .userName("sanghee")
            .password("13579")
            .build();

    /**
     * 회원가입
     * 성공
     * 실패 - userName 중복
     */
    @Test
    @WithMockUser
    @DisplayName("회원가입 success")
    void join() throws Exception {

        when(userService.join(any())).thenReturn(mock(UserDto.class));
        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)// 타입
                .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk()); // OK, 200반환
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 error - userName 중복")
    void join_fail() throws Exception {

        when(userService.join(any())).thenThrow(new AppException(ErrorCode.DUPLICATED_USER_NAME, "해당 userName이 중복됩니다."));
        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON) // 타입
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    /**
     * 로그인
     * 성공
     * 실패1 - userName 없음
     * 실패2 - password 틀림
     */
    @Test
    @WithMockUser
    @DisplayName("로그인 success")
    void login_success() throws Exception {
        String userName = "sanghee";
        String password = "13579";
        // 무엇을 보내서 : name, pw
        when(userService.login(any(), any())).thenReturn("token");
        // 무엇을 받을까? : USERNAME_NOT_FOUND
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    @DisplayName("로그인 _ username이 없음")
    void login_id_fail() throws Exception {
        String userName = "sanghee";
        String password = "13579";
        // 무엇을 보내서 : name, pw
        when(userService.login(any(), any())).thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, ""));
        // 무엇을 받을까? : USERNAME_NOT_FOUND(404)
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("로그인 _ password가 틀림")
    void login_password_fail() throws Exception {
        String userName = "sanghee";
        String password = "13579";
        // 무엇을 보내서 : name, pw
        when(userService.login(any(), any())).thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, ""));
        // 무엇을 받을까? : INVALID_PASSWORD
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}