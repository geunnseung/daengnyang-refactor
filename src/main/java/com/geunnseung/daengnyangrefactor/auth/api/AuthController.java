package com.geunnseung.daengnyangrefactor.auth.api;

import com.geunnseung.daengnyangrefactor.auth.api.dto.request.SignUpRequest;
import com.geunnseung.daengnyangrefactor.auth.api.dto.response.SignUpResponse;
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
}
