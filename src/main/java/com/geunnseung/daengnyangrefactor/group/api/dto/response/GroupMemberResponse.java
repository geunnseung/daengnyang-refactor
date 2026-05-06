package com.geunnseung.daengnyangrefactor.group.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.UserGroup;
import com.geunnseung.daengnyangrefactor.group.domain.UserGroupRole;
import java.time.LocalDateTime;

public record GroupMemberResponse(
        Long userId,
        String nickname,
        UserGroupRole role,
        LocalDateTime joinedAt
) {

    public static GroupMemberResponse from(final UserGroup userGroup) {
        return new GroupMemberResponse(
                userGroup.getUser().getId(),
                userGroup.getUser().getNickname(),
                userGroup.getRole(),
                userGroup.getCreatedAt()
        );
    }
}
