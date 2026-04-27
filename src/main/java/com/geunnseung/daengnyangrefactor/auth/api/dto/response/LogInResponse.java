package com.geunnseung.daengnyangrefactor.auth.api.dto.response;

import com.geunnseung.daengnyangrefactor.user.domain.User;

public record LogInResponse(
        Long userId,
        String nickname,
        TokenResponse token
) {

    public static LogInResponse of(
            final User user,
            final TokenResponse tokenResponse
    ) {
        return new LogInResponse(
                user.getId(),
                user.getNickname(),
                tokenResponse
        );
    }
}
