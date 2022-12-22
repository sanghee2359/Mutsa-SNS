package com.first.bulletinboard.domain.dto.user;

import com.first.bulletinboard.domain.entity.User;
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
    public User toEntity(String password){
        return User.builder()
                .userName(this.userName)
                .password(password)
                .build();
    }
}
