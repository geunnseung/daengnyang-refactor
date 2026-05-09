package com.geunnseung.daengnyangrefactor.comment.api.dto.response;

import com.geunnseung.daengnyangrefactor.comment.domain.Comment;
import java.time.LocalDateTime;

public record CommentCreateResponse(

        Long id,
        Long petPostId,
        Long authorId,
        String authorNickname,
        String content,
        LocalDateTime createdAt
) {

    public static CommentCreateResponse from(final Comment comment) {
        return new CommentCreateResponse(
                comment.getId(),
                comment.getPetPost().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getNickname(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}
