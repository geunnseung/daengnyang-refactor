package com.geunnseung.daengnyangrefactor.group.api.dto.request;

import com.geunnseung.daengnyangrefactor.group.service.command.GroupCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GroupCreateRequest(

        @NotBlank(message = "그룹 이름은 필수입니다.")
        @Size(max = 30, message = "그룹 이름은 30자 이하여야 합니다.")
        String name,

        @Size(max = 255, message = "그룹에 대한 설명은 255자 이하여야 합니다.")
        String description
) {

    public GroupCreateCommand toCommand() {
        return new GroupCreateCommand(
                name,
                description
        );
    }
}
