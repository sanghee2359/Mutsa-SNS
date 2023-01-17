package com.first.bulletinboard.domain.dto.comment;

import com.first.bulletinboard.domain.entity.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CommentReadResponse {
    private Long id;
    private String comment;
    private String userName;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    public static CommentReadResponse of(CommentDto comment) {
        return CommentReadResponse.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUserName())
                .postId(comment.getPostId())
                .createdAt(comment.getCreatedAt())
                .lastModifiedAt(comment.getLastModifiedAt())
                .build();
    }
}
