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
public class CommentUpdateResponse {
    private Long id;
    private String comment;
    private String userName;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static CommentUpdateResponse of(CommentDto dto){
        return CommentUpdateResponse.builder()
                .id(dto.getId())
                .comment(dto.getComment())
                .userName(dto.getUserName())
                .postId(dto.getPostId())
                .createdAt(dto.getCreatedAt())
                .lastModifiedAt(dto.getLastModifiedAt())
                .build();
    }

}
