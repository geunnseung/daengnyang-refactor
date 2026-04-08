package com.geunnseung.daengnyangrefactor.comment.service;

import com.geunnseung.daengnyangrefactor.comment.api.dto.request.CommentCreateRequest;
import com.geunnseung.daengnyangrefactor.comment.api.dto.response.CommentCreateResponse;
import com.geunnseung.daengnyangrefactor.comment.domain.Comment;
import com.geunnseung.daengnyangrefactor.comment.repository.CommentRepository;
import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.group.repository.UserGroupRepository;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import com.geunnseung.daengnyangrefactor.petpost.domain.PetPost;
import com.geunnseung.daengnyangrefactor.petpost.repository.PetPostRepository;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import com.geunnseung.daengnyangrefactor.user.repository.UserRepository;
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
            final CommentCreateRequest request
    ) {
        PetPost petPost = petPostRepository.findByIdAndDeletedAtIsNull(petPostId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_POST_NOT_FOUND));
        validatePetPostAccessible(userId, petPost);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.USER_NOT_FOUND));

        Comment comment = Comment.create(petPost, user, request.content());
        commentRepository.save(comment);

        return new CommentCreateResponse(
                comment.getId(),
                petPost.getId(),
                user.getId(),
                user.getNickname(),
                comment.getContent(),
                comment.getCreatedAt()
        );
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
}
