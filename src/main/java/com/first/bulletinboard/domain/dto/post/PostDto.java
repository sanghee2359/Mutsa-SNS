package com.first.bulletinboard.domain.dto.post;

import com.first.bulletinboard.domain.entity.user.User;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostDto {
    private int id;
    private User user;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
