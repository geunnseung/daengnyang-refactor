package com.geunnseung.daengnyangrefactor.auth.token;

import com.geunnseung.daengnyangrefactor.user.domain.User;
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
}