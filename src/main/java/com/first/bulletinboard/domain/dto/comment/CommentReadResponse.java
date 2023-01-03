package com.first.bulletinboard.domain.dto.comment;

import com.first.bulletinboard.domain.dto.post.PostReadResponse;
import com.first.bulletinboard.domain.entity.Comment;
import com.first.bulletinboard.domain.entity.Post;

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
