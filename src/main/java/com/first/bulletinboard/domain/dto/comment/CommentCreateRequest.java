package com.first.bulletinboard.domain.dto.comment;

import com.first.bulletinboard.domain.entity.Comment;
import com.first.bulletinboard.domain.entity.Post;
import com.first.bulletinboard.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommentCreateRequest {
    private String comment;
    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .comment(this.comment)
                .user(user)
                .post(post)
                .build();
    }
}
