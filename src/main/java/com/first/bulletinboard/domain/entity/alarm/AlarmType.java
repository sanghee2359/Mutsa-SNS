package com.first.bulletinboard.domain.entity.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlarmType {
    NEW_COMMENT_ON_POST("new like!"),
    NEW_LIKE_ON_POST("new comment!");

    private final String message;
}
