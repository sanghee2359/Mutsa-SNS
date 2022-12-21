package com.first.bulletinboard.domain;

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
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String password;
    private String userName;
    private Timestamp deletedAt;
    private Timestamp registerdAt; // 가입한 시간
    private Timestamp updatedAt;
    private UserRole role;
}



