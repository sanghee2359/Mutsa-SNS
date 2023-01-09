package com.first.bulletinboard.domain.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CommentCreateResponse {
    private Integer id;
    private String comment;
    private String userName;
    private Integer postId;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static CommentCreateResponse of(CommentDto dto){
        return CommentCreateResponse.builder()
                .id(dto.getId())
                .comment(dto.getComment())
                .userName(dto.getUserName())
                .postId(dto.getPostId())
                .createdAt(dto.getCreatedAt())
                .lastModifiedAt(dto.getLastModifiedAt())
                .build();
    }
}
