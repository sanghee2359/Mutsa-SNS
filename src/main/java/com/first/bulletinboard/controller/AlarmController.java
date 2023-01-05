package com.first.bulletinboard.controller;

import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.domain.dto.alarm.AlarmDto;
import com.first.bulletinboard.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/alarms")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;
    // 모든 user이 권한을 가진다.
    // login한 user이 자신이 받은 alarm list를 출력
    @GetMapping
    public Response<Page<AlarmDto>> alarmList(Authentication authentication) {
        Page<AlarmDto> dto = alarmService.findAllAlarms(authentication.getName());
        return Response.success(dto);
    }
}
