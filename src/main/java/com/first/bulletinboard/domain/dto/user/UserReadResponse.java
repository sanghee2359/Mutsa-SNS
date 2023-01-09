package com.first.bulletinboard.domain.dto.user;

import com.first.bulletinboard.domain.entity.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserReadResponse {
    private int id;
    private String userName;
    private UserRole role;

    public static UserReadResponse of(UserDto dto) {
        UserReadResponse response = UserReadResponse.builder()
                .id(dto.getId())
                .userName(dto.getUserName())
                .role(dto.getRole())
                .build();
        return response;
    }
}
