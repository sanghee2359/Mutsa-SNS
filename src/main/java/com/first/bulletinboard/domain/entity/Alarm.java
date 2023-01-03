package com.first.bulletinboard.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "alarm")
public class Alarm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 알람을 받은 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

//    @Enumerated(EnumType.STRING)
//    private AlarmType alarmType; // like인지 comment인지

    private Integer fromUserId;
    private Integer targetId; // post.id
    private String text;
}