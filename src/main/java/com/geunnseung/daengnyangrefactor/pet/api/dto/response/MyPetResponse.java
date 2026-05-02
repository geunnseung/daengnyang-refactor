package com.geunnseung.daengnyangrefactor.pet.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import com.geunnseung.daengnyangrefactor.pet.domain.PetGender;
import com.geunnseung.daengnyangrefactor.pet.domain.PetSpecies;
import java.time.LocalDate;

public record MyPetResponse(

        Long id,
        String name,
        PetSpecies species,
        PetGender gender,
        LocalDate birthDate,
        String profileImageUrl,
        Long groupId,
        String groupName
) {

    public static MyPetResponse from(final Pet pet) {
        Group group = pet.getGroup();

        return new MyPetResponse(
                pet.getId(),
                pet.getName(),
                pet.getSpecies(),
                pet.getGender(),
                pet.getBirthDate(),
                pet.getProfileImageUrl(),
                group == null ? null : group.getId(),
                group == null ? null : group.getName()
        );
    }
}
