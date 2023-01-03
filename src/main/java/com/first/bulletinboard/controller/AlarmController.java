package com.first.bulletinboard.controller;

import com.first.bulletinboard.domain.Response;
import com.first.bulletinboard.domain.dto.alarm.AlarmDto;
import com.first.bulletinboard.domain.entity.alarm.Alarm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/alarms")
@RequiredArgsConstructor
public class AlarmController {
    /*@GetMapping
    public Response<AlarmDto> alarmList(@AuthenticationPrincipal ) {
        return Response.success();
    }*/
}
