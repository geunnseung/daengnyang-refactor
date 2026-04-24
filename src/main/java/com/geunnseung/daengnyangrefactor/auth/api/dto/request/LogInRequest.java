package com.geunnseung.daengnyangrefactor.auth.api.dto.request;

import com.geunnseung.daengnyangrefactor.auth.service.command.LogInCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LogInRequest(

        @NotBlank(message = "이메일은 필수입니다.")
        @Size(max = 100, message = "이메일은 100자 이하로 입력해주세요.")
        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 10, max = 64, message = "비밀번호는 10자 이상 64자 이하여야 합니다.")
        String password
) {

    public LogInCommand toCommand() {
        return new LogInCommand(
                email,
                password
        );
    }
}
