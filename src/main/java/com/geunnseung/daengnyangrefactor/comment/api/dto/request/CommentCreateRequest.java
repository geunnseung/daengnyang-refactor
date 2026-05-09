package com.geunnseung.daengnyangrefactor.comment.api.dto.request;

import com.geunnseung.daengnyangrefactor.comment.service.command.CommentCreateCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(

        @NotBlank(message = "댓글 내용은 필수입니다.")
        @Size(max = 100, message = "댓글은 최대 100자까지 입력할 수 있습니다.")
        String content
) {

    public CommentCreateCommand toCommand() {
        return new CommentCreateCommand(content);
    }
}
