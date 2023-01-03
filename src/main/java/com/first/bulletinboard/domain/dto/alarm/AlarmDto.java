package com.first.bulletinboard.domain.dto.alarm;

import com.first.bulletinboard.domain.entity.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AlarmDto { // 변환을 위한 dto
    private int id;
    private AlarmDto alarmType;
    private Integer fromUserId;// 알림을 발생시킨 user id)
    private Integer targetId;// 알림이 발생된 post id)
    private String text;


}