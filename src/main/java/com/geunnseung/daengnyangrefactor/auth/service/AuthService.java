package com.geunnseung.daengnyangrefactor.auth.service;

import com.geunnseung.daengnyangrefactor.auth.api.dto.request.LogInRequest;
import com.geunnseung.daengnyangrefactor.auth.api.dto.request.LogOutRequest;
import com.geunnseung.daengnyangrefactor.auth.api.dto.request.ReissueRequest;
import com.geunnseung.daengnyangrefactor.auth.api.dto.request.SignUpRequest;
import com.geunnseung.daengnyangrefactor.auth.api.dto.response.LogInResponse;
import com.geunnseung.daengnyangrefactor.auth.api.dto.response.SignUpResponse;
import com.geunnseung.daengnyangrefactor.auth.api.dto.response.TokenResponse;
import com.geunnseung.daengnyangrefactor.auth.domain.RefreshToken;
import com.geunnseung.daengnyangrefactor.auth.repository.RefreshTokenRepository;
import com.geunnseung.daengnyangrefactor.auth.token.AccessTokenProvider;
import com.geunnseung.daengnyangrefactor.auth.token.RefreshTokenProvider;
import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import com.geunnseung.daengnyangrefactor.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private static final String TOKEN_TYPE = "Bearer";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenProvider accessTokenProvider;
    private final RefreshTokenProvider refreshTokenProvider;

    @Transactional
    public SignUpResponse signUp(final SignUpRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DaengnyangException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.createWithPassword(
                request.email(),
                encodedPassword,
                request.nickname()
        );

        User savedUser = userRepository.save(user);

        return new SignUpResponse(
                savedUser.getId(),
                savedUser.getNickname()
        );
    }

    @Transactional
    public LogInResponse logIn(final LogInRequest request) {
        User user = authenticateUser(request);

        revokeActiveRefreshTokens(user);

        TokenResponse tokenResponse = issueToken(user);

        return new LogInResponse(
                user.getId(),
                user.getNickname(),
                tokenResponse
        );
    }

    @Transactional
    public TokenResponse reissue(final ReissueRequest request) {
        RefreshToken refreshToken = findAvailableRefreshToken(request.refreshToken());
        User user = refreshToken.getUser();

        refreshToken.revoke();

        return issueToken(user);
    }

    @Transactional
    public void logOut(final LogOutRequest request) {
        RefreshToken refreshToken = findAvailableRefreshToken(request.refreshToken());

        refreshToken.revoke();
    }

    private User authenticateUser(final LogInRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new DaengnyangException(ErrorCode.INVALID_LOGIN_CREDENTIALS));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new DaengnyangException(ErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        return user;
    }

    private RefreshToken findAvailableRefreshToken(final String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (!refreshToken.isAvailable(LocalDateTime.now())) {
            throw new DaengnyangException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        return refreshToken;
    }

    private void revokeActiveRefreshTokens(final User user) {
        List<RefreshToken> activeRefreshTokens = refreshTokenRepository.findAllByUserAndRevokedFalse(user);
        activeRefreshTokens.forEach(RefreshToken::revoke);
    }

    private TokenResponse issueToken(final User user) {
        String accessToken = accessTokenProvider.createToken(user);
        String refreshTokenValue = refreshTokenProvider.createToken();

        saveRefreshToken(user, refreshTokenValue);

        return new TokenResponse(
                accessToken,
                refreshTokenValue,
                TOKEN_TYPE
        );
    }

    private void saveRefreshToken(
            final User user,
            final String refreshTokenValue
    ) {
        RefreshToken refreshToken = RefreshToken.issue(
                user,
                refreshTokenValue,
                refreshTokenProvider.calculateExpiresAt()
        );

        refreshTokenRepository.save(refreshToken);
    }
}
