package com.geunnseung.daengnyangrefactor.auth.service.command;

public record LogInCommand(

        String email,
        String password
) {
}
