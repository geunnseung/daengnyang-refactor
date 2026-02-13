package com.geunnseung.daengnyangrefactor.auth.service;

import com.geunnseung.daengnyangrefactor.auth.api.dto.request.SignUpRequest;
import com.geunnseung.daengnyangrefactor.auth.api.dto.response.SignUpResponse;
import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import com.geunnseung.daengnyangrefactor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpResponse signUp(final SignUpRequest request) {
        if(userRepository.existsByEmail((request.email())) {
            throw new DaengnyangException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = User.createWithPassword(
                request.email(),
                encodedPassword,
                request.nickname()
        );
    }
}
