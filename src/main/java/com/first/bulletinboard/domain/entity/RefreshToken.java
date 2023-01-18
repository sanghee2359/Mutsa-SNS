package com.first.bulletinboard.domain.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
public class RefreshToken {
    public static final Long DEFAULT_TTL = 1L; // time to live
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String refreshToken;
    private Long userId;

    @TimeToLive
    private Long expiration = DEFAULT_TTL;
    public RefreshToken(final String refreshToken, final Long memberId, Long expiration) {
        this.refreshToken = refreshToken;
        this.userId = memberId;
        this.expiration = expiration;
    }
    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getMemberId() {
        return userId;
    }
}
