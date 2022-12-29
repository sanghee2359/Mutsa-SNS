package com.first.bulletinboard.controller;

import com.first.bulletinboard.service.AlgorithmService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class HelloControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    AlgorithmService algorithmService;

    /*@Test
    @WithMockUser
    @DisplayName("hello에서 이름이 잘나오는지")
    void hello() throws Exception {
        mockMvc.perform(post("/api/v1/hello"))
                .with(csrf())
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("정상희"));
    }*/
}