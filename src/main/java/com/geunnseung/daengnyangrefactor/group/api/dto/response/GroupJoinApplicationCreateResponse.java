package com.geunnseung.daengnyangrefactor.group.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplicationStatus;

public record GroupJoinApplicationCreateResponse(
        Long id,
        Long groupId,
        GroupJoinApplicationStatus status
) {
}
