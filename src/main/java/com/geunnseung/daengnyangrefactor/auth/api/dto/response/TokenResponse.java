package com.geunnseung.daengnyangrefactor.auth.api.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
