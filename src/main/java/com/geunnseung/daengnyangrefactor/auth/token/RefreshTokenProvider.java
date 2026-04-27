package com.geunnseung.daengnyangrefactor.auth.token;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenProvider {

    private static final int TOKEN_BYTE_LENGTH = 64;

    private final SecureRandom secureRandom = new SecureRandom();
    private final long expirationDays;

    public RefreshTokenProvider(
            @Value("${auth.refresh-token-expiration-days:7}") final long expirationDays
    ) {
        this.expirationDays = expirationDays;
    }

    public String createToken() {
        byte[] bytes = new byte[TOKEN_BYTE_LENGTH];
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    public Instant calculateExpiresAt() {
        return Instant.now().plus(expirationDays, ChronoUnit.DAYS);
    }
}
