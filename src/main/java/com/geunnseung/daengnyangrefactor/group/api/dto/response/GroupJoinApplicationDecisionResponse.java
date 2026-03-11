package com.geunnseung.daengnyangrefactor.group.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplicationStatus;

public record GroupJoinApplicationDecisionResponse(
        Long groupJoinApplicationId,
        GroupJoinApplicationStatus status
) {
}
