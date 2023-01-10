package com.first.bulletinboard.controller;

import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.domain.dto.alarm.AlarmDto;
import com.first.bulletinboard.domain.dto.alarm.AlarmReadResponse;
import com.first.bulletinboard.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/alarms")
@RequiredArgsConstructor
public class AlarmController {
    private final AlarmService alarmService;
    @GetMapping
    public Response<Page<AlarmReadResponse>> findAlarmList(Authentication authentication, @PageableDefault(size = 10, sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {
        Page<AlarmDto> dto = alarmService.findAllAlarms(authentication.getName(), pageable);
        return Response.success(dto.map(AlarmReadResponse::of));
    }
}
