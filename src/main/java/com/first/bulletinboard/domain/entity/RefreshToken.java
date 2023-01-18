package com.first.bulletinboard.domain.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@RedisHash(value = "refreshToken", timeToLive = 60) // 데이터 저장 시간 60초: 만료 확인을 위해 짧게 설정
@AllArgsConstructor
@Getter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String refreshToken;
    private Long userId;

  /*  public RefreshToken(final String refreshToken, final Long memberId) {
        this.refreshToken = refreshToken;
        this.userId = memberId;
    }
*/
    /*public String getRefreshToken() {
        return refreshToken;
    }

    public Long getMemberId() {
        return userId;
    }*/
}
