package com.first.bulletinboard.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostUpdateResponse {
    private String message;
    private Long postId;
}
