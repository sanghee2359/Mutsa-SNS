package com.first.bulletinboard.domain.entity;

import com.first.bulletinboard.domain.dto.user.UserDto;
import com.first.bulletinboard.domain.dto.user.UserRole;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private int id;
    @Column(unique = true)
    private String userName;
    private String password;
    private LocalDateTime removedAt;
    private LocalDateTime registerdAt; // 가입한 시간
    private LocalDateTime updatedAt;
    private UserRole role;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> comments = new ArrayList<>();


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> likes = new ArrayList<>();


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();


    public UserDto toUserDto() {
        return UserDto.builder()
                .id(this.id)
                .userName(this.userName)
                .password(this.password)
                .role(this.role)
                .build();
    }
}



