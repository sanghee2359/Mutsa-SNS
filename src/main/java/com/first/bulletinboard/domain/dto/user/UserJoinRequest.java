package com.first.bulletinboard.domain.dto.user;

import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.domain.entity.user.UserRole;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Slf4j
public class UserJoinRequest {
    private String userName;
    private String password;
    public User toEntity(String password, UserRole userRole){
        return User.builder()
                .userName(this.userName)
                .password(password)
                .role(userRole)
                .build();
    }

}
