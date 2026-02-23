package com.geunnseung.daengnyangrefactor.auth.token;

import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenProvider {

    private final SecretKey secretKey;
    private final long expirationMillis;

    public AccessTokenProvider(
            @Value("${jwt.access-token-secret}") final String secretKey,
            @Value("${jwt.access-token-expiration-millis}") final long expirationMillis) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMillis;
    }

    public String createToken(final User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(expirationMillis);

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("role", user.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey)
                .compact();
    }

    public void validateToken(final String token) {
        parseClaims(token);
    }

    public Long getUserId(final String token) {
        String subject = parseClaims(token).getSubject();

        try {
            return Long.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new DaengnyangException(ErrorCode.INVALID_ACCESS_TOKEN, exception);
        }
    }

    private Claims parseClaims(final String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException exception) {
            throw new DaengnyangException(ErrorCode.INVALID_ACCESS_TOKEN, exception);
        }
    }
}