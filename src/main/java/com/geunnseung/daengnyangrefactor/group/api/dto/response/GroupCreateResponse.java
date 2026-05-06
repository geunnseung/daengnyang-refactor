package com.geunnseung.daengnyangrefactor.group.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.Group;

public record GroupCreateResponse(
        Long id,
        String name
) {

    public static GroupCreateResponse from(final Group group) {
        return new GroupCreateResponse(
                group.getId(),
                group.getName()
        );
    }
}
