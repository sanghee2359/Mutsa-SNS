package com.first.bulletinboard.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class UserJoinResponse {
    private int userId;
    private String userName;
}
