package com.geunnseung.daengnyangrefactor.pet.api.dto.response;

public record PetGroupCreateResponse(

        Long petId,
        Long groupId,
        String groupName
) {
}
