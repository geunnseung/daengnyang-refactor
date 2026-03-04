package com.geunnseung.daengnyangrefactor.group.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.UserGroupRole;

public record GroupDetailResponse(
        Long id,
        String name,
        String description,
        UserGroupRole role
) {
}
