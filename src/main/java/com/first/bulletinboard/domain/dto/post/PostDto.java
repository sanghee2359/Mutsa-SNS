package com.first.bulletinboard.domain.dto.post;

import com.first.bulletinboard.domain.entity.post.Post;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostDto {
    private Long id;
    private String userName;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static Page<PostDto> toPostList(Page<Post> posts) {
        return posts.map(post -> PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .body(post.getBody())
                .userName(post.getUser().getUsername())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build());
    }

}
