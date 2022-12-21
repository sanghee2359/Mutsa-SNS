package com.first.bulletinboard.domain.dto;

import com.first.bulletinboard.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserJoinResponse {
    private String userName;
    private String password;

}
