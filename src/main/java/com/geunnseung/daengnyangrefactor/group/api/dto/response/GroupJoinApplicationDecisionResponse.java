package com.geunnseung.daengnyangrefactor.group.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplication;
import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplicationStatus;

public record GroupJoinApplicationDecisionResponse(

        Long groupJoinApplicationId,
        GroupJoinApplicationStatus status
) {

    public static GroupJoinApplicationDecisionResponse from(final GroupJoinApplication application) {
        return new GroupJoinApplicationDecisionResponse(
                application.getId(),
                application.getStatus()
        );
    }
}
