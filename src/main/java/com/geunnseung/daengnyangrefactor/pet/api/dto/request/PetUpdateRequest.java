package com.geunnseung.daengnyangrefactor.pet.api.dto.request;

import com.geunnseung.daengnyangrefactor.pet.domain.PetGender;
import com.geunnseung.daengnyangrefactor.pet.domain.PetSpecies;
import com.geunnseung.daengnyangrefactor.pet.service.command.PetUpdateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record PetUpdateRequest(

        @NotBlank(message = "반려동물 이름은 필수입니다.")
        @Size(max = 30, message = "반려동물 이름은 최대 30자까지 입력할 수 있습니다.")
        String name,

        @NotNull(message = "반려동물 종류는 필수입니다.")
        PetSpecies species,

        @NotNull(message = "반려동물의 성별은 필수입니다.")
        PetGender gender,

        LocalDate birthDate,

        @Size(max = 500, message = "프로필 이미지 URL을 확인해주세요.")
        String profileImageUrl
) {

    public PetUpdateCommand toCommand() {
        return new PetUpdateCommand(
                name,
                species,
                gender,
                birthDate,
                profileImageUrl
        );
    }
}
