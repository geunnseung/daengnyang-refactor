package com.geunnseung.daengnyangrefactor.auth.api.dto.response;

import com.geunnseung.daengnyangrefactor.user.domain.User;

public record SignUpResponse(
        Long id,
        String nickname
) {

    public static SignUpResponse from(final User user) {
        return new SignUpResponse(
                user.getId(),
                user.getNickname()
        );
    }
}
