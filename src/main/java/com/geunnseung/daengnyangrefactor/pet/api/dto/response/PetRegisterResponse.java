package com.geunnseung.daengnyangrefactor.pet.api.dto.response;

import com.geunnseung.daengnyangrefactor.pet.domain.Pet;

public record PetRegisterResponse(

        Long id,
        String name
) {

    public static PetRegisterResponse from(final Pet pet) {
        return new PetRegisterResponse(
                pet.getId(),
                pet.getName()
        );
    }
}
