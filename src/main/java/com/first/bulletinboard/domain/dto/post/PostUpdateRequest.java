package com.first.bulletinboard.domain.dto.post;

import com.first.bulletinboard.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PostUpdateRequest {
    private String title;
    private String body;

    public Post toEntity() {
        return Post.builder()
                .title(this.title)
                .body(this.body)
                .build();
    }

}
