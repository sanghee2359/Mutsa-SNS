package com.first.bulletinboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.bulletinboard.domain.dto.post.PostCreateRequest;
import com.first.bulletinboard.domain.dto.post.PostDto;
import com.first.bulletinboard.domain.entity.Post;
import com.first.bulletinboard.domain.entity.User;
import com.first.bulletinboard.service.PostService;
import com.first.bulletinboard.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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


    // create
    @Test
    @WithMockUser
    @DisplayName("create Post success")
    public void createPost() throws Exception {
        given(postService.create(any(PostCreateRequest.class),any(String.class)))
                .willReturn(new PostDto(1, user, "testTitle", "testBody", null, null));

//        when(postService.create(any(),any())).thenReturn(mock(PostCreateRequest.class));
        mockMvc.perform(post("/api/v1/posts")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("SUCCESS"))
                .andExpect(jsonPath("$.result.postId").value(1))
                .andExpect(jsonPath("$.result.message").value("포스트 작성 완료"))
                .andDo(print());
    }

    /*@Test
    @WithMockUser
    @DisplayName("GET /posts/1 로 조회")
    void postReadOne() {
        // 조회 성공 - id, title, body, userName 4가지 항목 확인
        when(postService.findById(any())).thenReturn(mock(Post.class));
        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)// 타입
                        .content(objectMapper.writeValueAsBytes()))
                .andDo(print())
                .andExpect(status().isOk());
    }*/
}