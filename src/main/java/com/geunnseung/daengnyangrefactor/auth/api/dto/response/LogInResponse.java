package com.geunnseung.daengnyangrefactor.auth.api.dto.response;

public record LogInResponse(
        Long userId,
        String nickname,
        String accessToken,
        String refreshToken,
        String tokenType
) {
}
