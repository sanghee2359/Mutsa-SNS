package com.first.bulletinboard.domain.entity.alarm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.first.bulletinboard.domain.dto.alarm.AlarmDto;
import com.first.bulletinboard.domain.entity.BaseEntity;
import com.first.bulletinboard.domain.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "alarm")
public class Alarm extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 알람을 받은 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @JsonProperty
    private AlarmType alarmType; // like인지 comment인지

    private Integer fromUserId;
    private Integer targetId; // post.id
    private String text;

    public AlarmDto toAlarmDto() {
        return AlarmDto.builder()
                .id(this.id)
                .alarmType(this.alarmType)
                .fromUserId(this.fromUserId)
                .targetId(this.targetId)
                .text(this.text)
                .createdAt(getCreatedAt())
                .build();
    }
}