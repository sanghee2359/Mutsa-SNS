package com.first.bulletinboard.domain.dto.comment;

import com.first.bulletinboard.domain.entity.comment.Comment;

public class CommentReadResponse {
    public static CommentDto fromEntity(Comment comment) {
        CommentDto response = CommentDto.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .userName(comment.getUser().getUsername())
                .postId(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .lastModifiedAt(comment.getLastModifiedAt())
                .build();
        return response;
    }
}
