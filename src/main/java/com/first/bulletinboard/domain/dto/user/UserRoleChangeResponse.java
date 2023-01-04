package com.first.bulletinboard.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserRoleChangeResponse {
    private int userId;
    private String message;
}
