package com.first.bulletinboard.domain.dto.post;

import com.first.bulletinboard.domain.entity.Post;
import com.first.bulletinboard.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostCreateRequest {
    private String title;
    private String body;

    public Post toEntity(User user) {
        return Post.builder()
                .id(user.getId())
                .user(user)
                .title(this.title)
                .body(this.body)
                .build();
    }
}
