package com.first.bulletinboard.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.first.bulletinboard.domain.dto.UserDto;
import com.first.bulletinboard.domain.dto.UserRole;
import com.first.bulletinboard.domain.entity.Post;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
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


    public UserDto toDto() {
        return UserDto.builder()
                .id(this.id)
                .userName(this.userName)
                .password(this.password)
                .build();
    }
}



