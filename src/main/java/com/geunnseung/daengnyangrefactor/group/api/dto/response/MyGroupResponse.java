package com.geunnseung.daengnyangrefactor.group.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.UserGroupRole;

public record MyGroupResponse(
        Long id,
        String name,
        String description,
        UserGroupRole role,
        Long petId,
        String petName
) {
}
