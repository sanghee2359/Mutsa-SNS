package com.first.bulletinboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.service.AlarmService;
import com.first.bulletinboard.service.PostService;
import com.first.bulletinboard.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlarmController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AlarmControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PostService postService;
    @MockBean
    UserService userService;
    @MockBean
    AlarmService alarmService;
    @Autowired
    ObjectMapper objectMapper;
    //-------------------------------------Alarm-------------------------------------
    /**
     * read test
     */
    /*@Test // error
    @WithMockUser
    @DisplayName("[GET] 알람 목록 조회 성공")
    void alarmList_success() throws Exception {
        when(alarmService.findAllAlarms(any(),any())).thenReturn();

        mockMvc.perform(get("/api/v1/alarms")
                        .with(csrf())
                        .param("size","20")
                        .param("sort","createdAt,desc"))
                .andDo(print())
                .andExpect(status().isOk());

        // parameter을 캡쳐하는 클래스
        ArgumentCaptor<Pageable> pageableArgumentCaptor = ArgumentCaptor.forClass(Pageable.class);

        verify(postService).findAllComments(1,pageableArgumentCaptor.capture());
        PageRequest pageRequest = (PageRequest) pageableArgumentCaptor.getValue();

        assertEquals(20, pageRequest.getPageSize());
        assertEquals(Sort.by("createdAt", "DESC"), pageRequest.withSort(Sort.by("createdAt", "DESC")).getSort());
    }*/
    @Test // error
    @WithAnonymousUser
    @DisplayName("[GET] 알람 목록 조회 실패(1) - 인증 실패")
    void alarmList_fail1() throws Exception {
        given(alarmService.findAllAlarms(any(), any()))
                .willThrow(new AppException(ErrorCode.INVALID_PERMISSION));

        mockMvc.perform(get("/api/v1/alarms")
                        .with(csrf())
                        .param("size","20")
                        .param("sort","createdAt,desc"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}