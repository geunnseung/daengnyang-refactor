package com.geunnseung.daengnyangrefactor.user.api.dto.response;

import com.geunnseung.daengnyangrefactor.user.domain.User;

public record MeResponse(
        Long id,
        String email,
        String nickname
) {

    public static MeResponse from(final User user) {
        return new MeResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname()
        );
    }
}
