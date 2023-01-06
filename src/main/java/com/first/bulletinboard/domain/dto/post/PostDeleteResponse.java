package com.first.bulletinboard.domain.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostDeleteResponse {
    private String message;
    private int postId;
}
