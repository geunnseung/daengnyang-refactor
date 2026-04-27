package com.geunnseung.daengnyangrefactor.auth.api.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken,
        String tokenType
) {

    private static final String BEARER_TYPE = "Bearer";

    public static TokenResponse bearer(
            final String accessToken,
            final String refreshToken
    ) {
        return new TokenResponse(
                accessToken,
                refreshToken,
                BEARER_TYPE
        );
    }
}
