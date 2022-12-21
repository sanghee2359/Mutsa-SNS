package com.first.bulletinboard.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "해당 UserName이 중복됩니다.");
    private HttpStatus status;
    private String message;
}
