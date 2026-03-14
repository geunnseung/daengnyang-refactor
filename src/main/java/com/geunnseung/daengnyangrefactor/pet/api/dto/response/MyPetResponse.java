package com.geunnseung.daengnyangrefactor.pet.api.dto.response;

import com.geunnseung.daengnyangrefactor.pet.domain.PetGender;
import com.geunnseung.daengnyangrefactor.pet.domain.PetSpecies;
import java.time.LocalDate;

public record MyPetResponse(

        Long id,
        String name,
        PetSpecies species,
        PetGender gender,
        LocalDate birthDate,
        String profileImageUrl
) {
}
