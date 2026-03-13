package com.geunnseung.daengnyangrefactor.group.api.dto.response;

import com.geunnseung.daengnyangrefactor.group.domain.UserGroupRole;
import java.time.LocalDateTime;

public record GroupMemberResponse(
        Long userId,
        String nickname,
        UserGroupRole role,
        LocalDateTime joinedAt
) {
}
