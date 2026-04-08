package com.geunnseung.daengnyangrefactor.comment.api.dto.response;

import java.time.LocalDateTime;

public record CommentCreateResponse(

        Long id,
        Long petPostId,
        Long authorId,
        String authorNickname,
        String content,
        LocalDateTime createdAt
) {
}
