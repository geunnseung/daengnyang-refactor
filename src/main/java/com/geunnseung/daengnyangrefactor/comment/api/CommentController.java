package com.geunnseung.daengnyangrefactor.comment.api;

import com.geunnseung.daengnyangrefactor.auth.support.AuthenticatedUser;
import com.geunnseung.daengnyangrefactor.auth.support.LoginUser;
import com.geunnseung.daengnyangrefactor.comment.api.dto.request.CommentCreateRequest;
import com.geunnseung.daengnyangrefactor.comment.api.dto.request.CommentUpdateRequest;
import com.geunnseung.daengnyangrefactor.comment.api.dto.response.CommentCreateResponse;
import com.geunnseung.daengnyangrefactor.comment.api.dto.response.CommentResponse;
import com.geunnseung.daengnyangrefactor.comment.service.CommentService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pet-posts/{petPostId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentCreateResponse> createComment(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petPostId,
            @Valid @RequestBody final CommentCreateRequest request
    ) {
        CommentCreateResponse response = commentService.createComment(
                authenticatedUser.id(),
                petPostId,
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petPostId
    ) {
        List<CommentResponse> responses = commentService.getComments(
                authenticatedUser.id(),
                petPostId
        );

        return ResponseEntity.ok(responses);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petPostId,
            @PathVariable final Long commentId,
            @Valid @RequestBody final CommentUpdateRequest request
    ) {
        CommentResponse response = commentService.updateComment(
                authenticatedUser.id(),
                petPostId,
                commentId,
                request
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petPostId,
            @PathVariable final Long commentId
    ) {
        commentService.deleteComment(
                authenticatedUser.id(),
                petPostId,
                commentId
        );

        return ResponseEntity.noContent().build();
    }
}
