package com.geunnseung.daengnyangrefactor.group.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplication;
import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplicationStatus;
import java.time.LocalDateTime;

public record GroupJoinApplicationResponse(
        Long id,
        Long requesterId,
        String requesterNickname,
        GroupJoinApplicationStatus status,
        LocalDateTime createdAt
) {

    public static GroupJoinApplicationResponse from(final GroupJoinApplication application) {
        return new GroupJoinApplicationResponse(
                application.getId(),
                application.getRequester().getId(),
                application.getRequester().getNickname(),
                application.getStatus(),
                application.getCreatedAt()
        );
    }
}
