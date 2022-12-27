package com.first.bulletinboard.domain.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserRole {
    ADMIN, USER;

    @JsonCreator // 대문자 변환
    public static UserRole from(String s) {
        return UserRole.valueOf(s.toUpperCase());
    }

}
