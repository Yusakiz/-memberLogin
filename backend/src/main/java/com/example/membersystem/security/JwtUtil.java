package com.example.membersystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具類別 (JwtUtil)
 * 負責：
 * 1. 產生 Token (加密簽發)
 * 2. 驗證 Token (檢查是否過期或被篡改)
 * 3. 從 Token 解析出會員 ID
 */
@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms:86400000}") long expirationMs) {
        // 使用 application.yml 裡設定的 secret 產生 HMAC-SHA 加密金鑰
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    // 1. 登入成功時，簽發一張 Token 給前端
    public String generateToken(Long memberId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(String.valueOf(memberId)) // 將會員 ID 放在主體 (Subject)
                .claim("username", username)       // 附帶帳號資訊
                .issuedAt(now)                     // 簽發時間
                .expiration(expiryDate)            // 過期時間
                .signWith(key)                     // 數位簽名
                .compact();
    }

    // 2. 從 Token 提取出會員 ID
    public Long getMemberIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    // 3. 驗證 Token 是否合法 (若過期或被篡改會丟出例外)
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
