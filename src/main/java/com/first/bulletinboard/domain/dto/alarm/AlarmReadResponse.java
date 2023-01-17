package com.first.bulletinboard.domain.dto.alarm;

import com.first.bulletinboard.domain.dto.comment.CommentReadResponse;
import com.first.bulletinboard.domain.entity.alarm.Alarm;
import com.first.bulletinboard.domain.entity.alarm.AlarmType;
import com.first.bulletinboard.domain.entity.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AlarmReadResponse {
    private Long id;
    private AlarmType alarmType;
    private Long fromUserId;// 알림을 발생시킨 user id)
    private Long targetId;// 알림이 발생된 post id)
    private String text;
    private LocalDateTime createdAt;

    public static AlarmReadResponse of(Alarm alarm) {
        return AlarmReadResponse.builder()
                .id(alarm.getId())
                .alarmType(alarm.getAlarmType())
                .fromUserId(alarm.getFromUserId())
                .targetId(alarm.getTargetId())
                .text(alarm.getText())
                .createdAt(alarm.getCreatedAt())
                .build();
    }
}
