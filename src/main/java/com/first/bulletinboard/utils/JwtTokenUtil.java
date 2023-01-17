package com.first.bulletinboard.utils;

import com.first.bulletinboard.domain.entity.user.User;
import com.first.bulletinboard.domain.entity.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
@Slf4j
public class JwtTokenUtil {
//    private final String SECRET = "hello";

    public JwtTokenUtil() {
        //secret 과 만기일 재설정
    }
    /*public static String getUserName(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().get("userName",String.class);
    }*/
    public Authentication getAuthentication(String token, String secretKey) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        Long id = Long.parseLong(claims.get("id").toString());
        String username = claims.get("userName").toString();
        String roleName = claims.get("role").toString();

        User user = User.builder()
                .id(id)
                .userName(username)
                .role(UserRole.valueOf(roleName))
                .build();

        log.info("user:{}",user.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
        log.info("authentication:{}",authentication.getAuthorities());
        return authentication;
    }
    /*public static boolean isExpired(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody().getExpiration().before(new Date());
    }*/
    public boolean validateToken(String token,String secretKey) {
        try {
            log.info("validate token 시작");
            log.info("secretKey {}",secretKey);
            /*Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token).getBody();*/

            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            /*log.info("validate id{}",claims.get("id", Long.class));
            log.info("validate name{}",claims.get("userName", String.class));
            log.info("validate role{}",claims.get("role", String.class));*/
            return true;
        } catch (io.jsonwebtoken.SignatureException | MalformedJwtException exception) {
            log.info("validateToken : 잘못된 시그니처");// 잘못된 jwt signature
        } catch (io.jsonwebtoken.ExpiredJwtException exception) {
            log.info("validateToken : jwt 만료");// jwt 만료
        } catch (io.jsonwebtoken.UnsupportedJwtException exception) {
            log.info("validateToken : 지원하지 않는 jwt");// 지원하지 않는 jwt
        } catch (IllegalArgumentException exception) {
            log.info("잘못된 jwt 토큰");// 잘못된 jwt 토큰
        }
        return false;
    }
    /*public static String createToken(String userName, String key, long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("userName", userName);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 만든 날짜
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs)) // 만료 날짜
                .signWith(SignatureAlgorithm.HS256, key) // HS256 알고리즘과 key로 암호화
                .compact();
    }*/
    public static String generateToken(User user,String key, long expireTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("id", user.getId());
        claims.put("userName", user.getUsername());
        claims.put("role", user.getRole().name());

        log.info("user:{}",user.getUsername());
        log.info("user id:{}",user.getId());
        log.info("user role:{}",user.getRole());
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        return token;
    }
}
