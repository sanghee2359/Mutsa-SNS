package com.first.bulletinboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.bulletinboard.domain.dto.post.PostCreateRequest;
import com.first.bulletinboard.domain.dto.post.PostDto;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.service.PostService;
import com.first.bulletinboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PostController.class)
class PostControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PostService postService;
    @MockBean
    UserService userService;
    @Autowired
    ObjectMapper objectMapper;

    User user = User.builder().build();
    PostCreateRequest request = PostCreateRequest.builder().title("testTitle").body("testBody").build();
    PostDto dto = Post.builder().id(1).build().toPostDto();

    /*@Test
    @WithMockUser
    @DisplayName("마이피드 조회 성공")
    void myfeed_success() throws Exception {
        String userName = "sanghee";
        String password = "13579";
        // 무엇을 보내서 : name, pw
        when(userService.login(any(), any())).thenReturn(Page<PostReadResponse.class>);
        // 무엇을 받을까? : USERNAME_NOT_FOUND
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isOk());
    }*/
    /*@Test
    @WithMockUser
    @DisplayName("마이피드 조회 실패")
    void myfeed_success() throws Exception {
        String userName = "sanghee";
        String password = "13579";
        // 무엇을 보내서 : name, pw
        when(userService.login(any(), any())).thenReturn(Page<PostReadResponse.class>);
        // 무엇을 받을까? : USERNAME_NOT_FOUND
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName,password))))
                .andDo(print())
                .andExpect(status().isOk());
    }*/

}