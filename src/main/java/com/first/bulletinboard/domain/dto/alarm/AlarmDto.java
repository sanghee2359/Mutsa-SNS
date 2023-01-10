package com.first.bulletinboard.domain.dto.alarm;

import com.first.bulletinboard.domain.dto.post.PostDto;
import com.first.bulletinboard.domain.entity.alarm.Alarm;
import com.first.bulletinboard.domain.entity.alarm.AlarmType;
import com.first.bulletinboard.domain.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AlarmDto {
    private int id;
    private AlarmType alarmType;
    private Integer fromUserId;// 알림을 발생시킨 user id)
    private Integer targetId;// 알림이 발생된 post id)
    private String text;
    private LocalDateTime createdAt;
    public static Page<AlarmDto> toAlarmList(Page<Alarm> alarms) {
        return alarms.map(alarm -> AlarmDto.builder()
                .alarmType(alarm.getAlarmType())
                .fromUserId(alarm.getFromUserId())
                .targetId(alarm.getTargetId())
                .text(alarm.getText())
                .createdAt(alarm.getCreatedAt())
                .build());
    }
}
