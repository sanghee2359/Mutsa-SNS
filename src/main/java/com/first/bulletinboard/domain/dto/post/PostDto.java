package com.first.bulletinboard.domain.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.first.bulletinboard.domain.entity.User;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
