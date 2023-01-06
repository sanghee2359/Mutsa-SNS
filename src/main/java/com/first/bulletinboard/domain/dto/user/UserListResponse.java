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
public class UserListResponse {
    private int id;
    private String userName;
    private UserRole role;

    public static UserListResponse of(UserDto dto) {
        UserListResponse response = UserListResponse.builder()
                .id(dto.getId())
                .userName(dto.getUserName())
                .role(dto.getRole())
                .build();
        return response;
    }
}
