package com.first.bulletinboard.domain.dto;

import com.first.bulletinboard.domain.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserJoinRequest {
    private String userName;
    private String password;
    public UserEntity toEntity(String password){
        return UserEntity.builder()
                .userName(this.getUserName())
                .password(password)
                .build();
    }
}
