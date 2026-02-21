package com.geunnseung.daengnyangrefactor.auth.api;

import com.geunnseung.daengnyangrefactor.auth.api.dto.request.LogInRequest;
import com.geunnseung.daengnyangrefactor.auth.api.dto.request.LogOutRequest;
import com.geunnseung.daengnyangrefactor.auth.api.dto.request.ReissueRequest;
import com.geunnseung.daengnyangrefactor.auth.api.dto.request.SignUpRequest;
import com.geunnseung.daengnyangrefactor.auth.api.dto.response.LogInResponse;
import com.geunnseung.daengnyangrefactor.auth.api.dto.response.SignUpResponse;
import com.geunnseung.daengnyangrefactor.auth.api.dto.response.TokenResponse;
import com.geunnseung.daengnyangrefactor.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(
            @Valid @RequestBody final SignUpRequest request
    ) {
        SignUpResponse response = authService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LogInResponse> logIn(
            @Valid @RequestBody final LogInRequest request
    ) {
        LogInResponse response = authService.logIn(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(
            @Valid @RequestBody final ReissueRequest request
    ) {
        TokenResponse response = authService.reissue(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut(
            @Valid @RequestBody final LogOutRequest request
    ) {
        authService.logOut(request);

        return ResponseEntity.noContent().build();
    }
}
