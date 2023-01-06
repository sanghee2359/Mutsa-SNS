package com.first.bulletinboard.domain.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.first.bulletinboard.domain.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PostReadResponse {
    private int id;
    private String title;
    private String body;
    private String userName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;
    public static PostReadResponse of(PostDto dto) {
        PostReadResponse response = PostReadResponse.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .body(dto.getBody())
                .userName(dto.getUserName())
                .createdAt(dto.getCreatedAt())
                .lastModifiedAt(dto.getLastModifiedAt())
                .build();
        return response;
    }
}
