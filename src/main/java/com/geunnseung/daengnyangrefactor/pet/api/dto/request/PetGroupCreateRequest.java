package com.geunnseung.daengnyangrefactor.pet.api.dto.request;

import com.geunnseung.daengnyangrefactor.pet.service.command.PetGroupCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PetGroupCreateRequest(

        @NotBlank(message = "그룹 이름은 필수입니다.")
        @Size(max = 30, message = "그룹 이름은 최대 30자까지 입력할 수 있습니다.")
        String name,

        @Size(max = 255, message = "그룹 설명은 최대 255자까지 입력할 수 있습니다.")
        String description
) {

    public PetGroupCreateCommand toCommand() {
        return new PetGroupCreateCommand(
                name,
                description
        );
    }
}
