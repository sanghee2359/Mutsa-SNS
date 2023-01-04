package com.first.bulletinboard.domain;

import com.first.bulletinboard.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private final ErrorCode errorCode;
    private final String message;
}
