package com.first.bulletinboard.domain.dto.post;

import com.first.bulletinboard.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostUpdateRequest {
    private String title;
    private String body;

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .body(body)
                .build();

    }

}
