package com.first.bulletinboard.domain.dto.post;

import com.first.bulletinboard.domain.entity.User;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PostDto {
    private int id;
    private User user;
    private String title;
    private String body;
}
