package com.geunnseung.daengnyangrefactor.group.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.group.domain.UserGroup;
import com.geunnseung.daengnyangrefactor.group.domain.UserGroupRole;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;

public record GroupDetailResponse(
        Long id,
        String name,
        String description,
        UserGroupRole role,
        Long petId,
        String petName
) {

    public static GroupDetailResponse of(
            final UserGroup userGroup,
            final Pet pet
    ) {
        Group group = userGroup.getGroup();

        return new GroupDetailResponse(
                group.getId(),
                group.getName(),
                group.getDescription(),
                userGroup.getRole(),
                pet.getId(),
                pet.getName()
        );
    }
}
