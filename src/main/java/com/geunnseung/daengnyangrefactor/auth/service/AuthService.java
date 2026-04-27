package com.geunnseung.daengnyangrefactor.auth.service;

import com.geunnseung.daengnyangrefactor.auth.api.dto.response.LogInResponse;
import com.geunnseung.daengnyangrefactor.auth.api.dto.response.SignUpResponse;
import com.geunnseung.daengnyangrefactor.auth.api.dto.response.TokenResponse;
import com.geunnseung.daengnyangrefactor.auth.domain.RefreshToken;
import com.geunnseung.daengnyangrefactor.auth.repository.RefreshTokenRepository;
import com.geunnseung.daengnyangrefactor.auth.service.command.LogInCommand;
import com.geunnseung.daengnyangrefactor.auth.service.command.SignUpCommand;
import com.geunnseung.daengnyangrefactor.auth.token.AccessTokenProvider;
import com.geunnseung.daengnyangrefactor.auth.token.RefreshTokenProvider;
import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import com.geunnseung.daengnyangrefactor.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
    public SignUpResponse signUp(final SignUpCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new DaengnyangException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(command.password());

        User user = User.createWithPassword(
                command.email(),
                encodedPassword,
                command.nickname()
        );
        User savedUser = saveUser(user);

        return new SignUpResponse(
                savedUser.getId(),
                savedUser.getNickname()
        );
    }

    @Transactional
    public LogInResponse logIn(final LogInCommand command) {
        User authenticatedUser = authenticateUser(command);
        User user = findUserForUpdate(authenticatedUser.getId());

        revokeActiveRefreshTokens(user);

        TokenResponse tokenResponse = issueToken(user);

        return new LogInResponse(
                user.getId(),
                user.getNickname(),
                tokenResponse
        );
    }

    @Transactional
    public TokenResponse reissue(final String refreshTokenValue) {
        RefreshToken refreshToken = findAvailableRefreshToken(refreshTokenValue);
        User user = refreshToken.getUser();

        refreshToken.revoke();

        return issueToken(user);
    }

    @Transactional
    public void logOut(final String refreshTokenValue) {
        RefreshToken refreshToken = findAvailableRefreshToken(refreshTokenValue);

        refreshToken.revoke();
    }

    private User saveUser(final User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new DaengnyangException(ErrorCode.EMAIL_ALREADY_EXISTS, ex);
        }
    }

    private User authenticateUser(final LogInCommand command) {
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new DaengnyangException(ErrorCode.INVALID_LOGIN_CREDENTIALS));

        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new DaengnyangException(ErrorCode.INVALID_LOGIN_CREDENTIALS);
        }

        return user;
    }

    private RefreshToken findAvailableRefreshToken(final String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenForUpdate(token)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.INVALID_REFRESH_TOKEN));

        if (!refreshToken.isAvailable(Instant.now())) {
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

    private User findUserForUpdate(final Long userId) {
        return userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.USER_NOT_FOUND));
    }
}
