package com.geunnseung.daengnyangrefactor.group.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplicationStatus;
import java.time.LocalDateTime;

public record GroupJoinApplicationResponse(
        Long id,
        Long requesterId,
        String requesterNickname,
        GroupJoinApplicationStatus status,
        LocalDateTime createdAt
) {
}
