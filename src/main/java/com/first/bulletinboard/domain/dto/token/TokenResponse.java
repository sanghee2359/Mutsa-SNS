package com.first.bulletinboard.domain.dto.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    private String refreshToken;
    private String accessToken;
}
