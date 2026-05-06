package com.geunnseung.daengnyangrefactor.group.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplication;
import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplicationStatus;

public record GroupJoinApplicationCreateResponse(
        Long id,
        Long groupId,
        GroupJoinApplicationStatus status
) {

    public static GroupJoinApplicationCreateResponse from(final GroupJoinApplication application) {
        return new GroupJoinApplicationCreateResponse(
                application.getId(),
                application.getGroup().getId(),
                application.getStatus()
        );
    }
}
