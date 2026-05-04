package com.geunnseung.daengnyangrefactor.pet.service.command;

import com.geunnseung.daengnyangrefactor.pet.domain.PetGender;
import com.geunnseung.daengnyangrefactor.pet.domain.PetSpecies;
import java.time.LocalDate;

public record PetUpdateCommand(

        String name,
        PetSpecies species,
        PetGender gender,
        LocalDate birthDate,
        String profileImageUrl
) {
}
