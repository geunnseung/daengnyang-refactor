package com.geunnseung.daengnyangrefactor.comment.service;

import com.geunnseung.daengnyangrefactor.comment.api.dto.response.CommentCreateResponse;
import com.geunnseung.daengnyangrefactor.comment.api.dto.response.CommentResponse;
import com.geunnseung.daengnyangrefactor.comment.domain.Comment;
import com.geunnseung.daengnyangrefactor.comment.repository.CommentRepository;
import com.geunnseung.daengnyangrefactor.comment.service.command.CommentCreateCommand;
import com.geunnseung.daengnyangrefactor.comment.service.command.CommentUpdateCommand;
import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.group.repository.UserGroupRepository;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import com.geunnseung.daengnyangrefactor.petpost.domain.PetPost;
import com.geunnseung.daengnyangrefactor.petpost.repository.PetPostRepository;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import com.geunnseung.daengnyangrefactor.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final PetPostRepository petPostRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentCreateResponse createComment(
            final Long userId,
            final Long petPostId,
            final CommentCreateCommand command
    ) {
        PetPost petPost = findPetPost(petPostId);
        validatePetPostAccessible(userId, petPost);

        User user = findUser(userId);

        Comment comment = Comment.create(petPost, user, command.content());
        commentRepository.save(comment);

        return CommentCreateResponse.from(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(final Long userId, final Long petPostId) {
        PetPost petPost = findPetPost(petPostId);
        validatePetPostAccessible(userId, petPost);

        List<Comment> comments = commentRepository.findAllWithAuthorByPetPostId(petPostId);

        return comments.stream()
                .map(CommentResponse::from)
                .toList();
    }

    @Transactional
    public CommentResponse updateComment(
            final Long userId,
            final Long petPostId,
            final Long commentId,
            final CommentUpdateCommand command
    ) {
        Comment comment = findComment(petPostId, commentId);
        validateCommentAuthor(userId, comment);

        comment.updateContent(command.content());

        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(
            final Long userId,
            final Long petPostId,
            final Long commentId
    ) {
        Comment comment = findComment(petPostId, commentId);
        validateCommentDeletable(userId, comment);

        comment.delete();
    }

    private PetPost findPetPost(final Long petPostId) {
        return petPostRepository.findWithPetAndOwnerAndGroupById(petPostId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_POST_NOT_FOUND));
    }

    private void validatePetPostAccessible(final Long userId, final PetPost petPost) {
        Pet pet = petPost.getPet();
        if (pet.getOwner().getId().equals(userId)) {
            return;
        }

        Group group = pet.getGroup();
        if (group != null && userGroupRepository.existsByUserIdAndGroupId(userId, group.getId())) {
            return;
        }

        throw new DaengnyangException(ErrorCode.PET_POST_NOT_FOUND);
    }

    private User findUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.USER_NOT_FOUND));
    }

    private Comment findComment(final Long petPostId, final Long commentId) {
        return commentRepository.findWithAuthorAndPetPostOwnerByIdAndPetPostId(
                        commentId,
                        petPostId
                )
                .orElseThrow(() -> new DaengnyangException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private void validateCommentAuthor(final Long userId, final Comment comment) {
        if (comment.getAuthor().getId().equals(userId)) {
            return;
        }

        throw new DaengnyangException(ErrorCode.COMMENT_ACCESS_DENIED);
    }

    private void validateCommentDeletable(final Long userId, final Comment comment) {
        if (comment.getAuthor().getId().equals(userId)) {
            return;
        }

        if (comment.getPetPost().getPet().getOwner().getId().equals(userId)) {
            return;
        }

        throw new DaengnyangException(ErrorCode.COMMENT_ACCESS_DENIED);
    }
}
