package com.geunnseung.daengnyangrefactor.user.api;

import com.geunnseung.daengnyangrefactor.auth.support.AuthenticatedUser;
import com.geunnseung.daengnyangrefactor.auth.support.LoginUser;
import com.geunnseung.daengnyangrefactor.user.api.dto.response.MeResponse;
import com.geunnseung.daengnyangrefactor.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<MeResponse> getMe(
            @LoginUser final AuthenticatedUser authenticatedUser
    ) {
        MeResponse response = userService.getMe(authenticatedUser.id());

        return ResponseEntity.ok(response);
    }
}
