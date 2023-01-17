package com.first.bulletinboard.domain.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentDeleteResponse {
    private String message;
    private Long id; // comment id
}
