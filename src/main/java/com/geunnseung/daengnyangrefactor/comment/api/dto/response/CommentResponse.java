package com.geunnseung.daengnyangrefactor.comment.api.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(

        Long id,
        Long authorId,
        String authorNickname,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
