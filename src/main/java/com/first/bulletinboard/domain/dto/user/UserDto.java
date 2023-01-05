package com.first.bulletinboard.domain.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.first.bulletinboard.domain.entity.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDto { // 변환을 위한 dto
    private int id;
    private UserRole role;
    private String userName;
    @JsonIgnore
    private String password;
}
