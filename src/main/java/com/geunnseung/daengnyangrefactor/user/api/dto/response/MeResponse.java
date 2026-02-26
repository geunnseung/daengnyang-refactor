package com.geunnseung.daengnyangrefactor.user.api.dto.response;

public record MeResponse(
        Long id,
        String email,
        String nickname
) {
}
