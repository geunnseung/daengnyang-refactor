package com.geunnseung.daengnyangrefactor.pet.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;

public record PetGroupCreateResponse(

        Long petId,
        Long groupId,
        String groupName
) {

    public static PetGroupCreateResponse of(
            final Pet pet,
            final Group group
    ) {
        return new PetGroupCreateResponse(
                pet.getId(),
                group.getId(),
                group.getName()
        );
    }
}
