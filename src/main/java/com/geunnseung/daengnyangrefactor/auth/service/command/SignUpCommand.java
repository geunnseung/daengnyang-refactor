package com.geunnseung.daengnyangrefactor.auth.service.command;

public record SignUpCommand(

        String email,
        String password,
        String nickname
) {
}
