package com.geunnseung.daengnyangrefactor.auth.api.dto.response;

public record LogInResponse(
        Long userId,
        String nickname,
        TokenResponse token
) {
}
