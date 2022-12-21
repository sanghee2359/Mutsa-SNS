package com.first.bulletinboard.domain;

import com.first.bulletinboard.domain.dto.UserDto;
import com.first.bulletinboard.domain.dto.UserRole;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true)
    private String userName;
    private String password;
    private Timestamp deletedAt;
    private Timestamp registerdAt; // 가입한 시간
    private Timestamp updatedAt;
    private UserRole role;

    public UserDto toDto() {
        return UserDto.builder()
                .id(this.id)
                .userName(this.userName)
                .password(this.password)
                .build();
    }
}



