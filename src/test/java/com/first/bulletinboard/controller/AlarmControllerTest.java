package com.first.bulletinboard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.first.bulletinboard.domain.dto.alarm.AlarmDto;
import com.first.bulletinboard.domain.dto.alarm.AlarmReadResponse;
import com.first.bulletinboard.domain.entity.alarm.Alarm;
import com.first.bulletinboard.domain.entity.alarm.AlarmType;
import com.first.bulletinboard.domain.entity.comment.Comment;
import com.first.bulletinboard.domain.entity.post.Post;
import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.exception.AppException;
import com.first.bulletinboard.exception.ErrorCode;
import com.first.bulletinboard.repository.AlarmRepository;
import com.first.bulletinboard.repository.CommentRepository;
import com.first.bulletinboard.service.AlarmService;
import com.first.bulletinboard.service.PostService;
import com.first.bulletinboard.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlarmController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Slf4j
class AlarmControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    PostService postService;
    @MockBean
    AlarmRepository alarmRepository;
    @MockBean
    UserService userService;
    @MockBean
    AlarmService alarmService;
    @Autowired
    ObjectMapper objectMapper;

    User user = User.builder().id(1).userName("user1").password("password1").build();
    Post post1 = Post.builder().id(1).title("title1").body("body1").user(user).build();
    Comment comment1 = Comment.builder().id(1).comment("comment1").user(user).post(post1).build();
//    Alarm -> user이 user에게 댓글 달았다고 가정
    Alarm alarm = Alarm.builder().alarmType(AlarmType.NEW_COMMENT_ON_POST).
            user(user).
            fromUserId(comment1.getUser().getId()).
            targetId(comment1.getPost().getId()).
            text("new comment!").
            build();

    //-------------------------------------Alarm-------------------------------------
    /**
     * read test
     */
    @Test // error
    @WithMockUser
    @DisplayName("[GET] 알람 목록 조회 성공")
    void alarmList_success() throws Exception {
        alarmRepository.save(alarm);
        List<Alarm> alarms = new ArrayList<>();
        alarms.add(alarm);

        PageRequest pageable = PageRequest.of(0,20,Sort.by("createdAt").descending());
        Page<Alarm> alarmList = new PageImpl<>(alarms, pageable,1);
        Page<AlarmDto> alarmPageList = AlarmDto.toAlarmList(alarmList);
        when(alarmService.findAllAlarms(any(),any(Pageable.class))).thenReturn(alarmPageList);

        mockMvc.perform(get("/api/v1/alarms")
                        .with(csrf())
                        .param("size","20")
                        .param("sort","createdAt,desc"))
                .andExpect(jsonPath("$.result.content[0].id").value(0))
                .andExpect(jsonPath("$.result.content[0].alarmType").value("NEW_COMMENT_ON_POST"))
                .andExpect(jsonPath("$.result.content[0].fromUserId").value(1))
                .andExpect(jsonPath("$.result.content[0].targetId").value(1))
                .andExpect(jsonPath("$.result.content[0].text").value("new comment!"))
                .andDo(print())
                .andExpect(status().isOk());

    }
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