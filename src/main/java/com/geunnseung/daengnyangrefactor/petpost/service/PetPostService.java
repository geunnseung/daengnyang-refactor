package com.geunnseung.daengnyangrefactor.petpost.service;

import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.global.storage.MediaStoragePort;
import com.geunnseung.daengnyangrefactor.global.storage.MediaUploadCommand;
import com.geunnseung.daengnyangrefactor.global.storage.MediaUploadResult;
import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.group.repository.UserGroupRepository;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import com.geunnseung.daengnyangrefactor.pet.repository.PetRepository;
import com.geunnseung.daengnyangrefactor.petpost.api.dto.request.PetPostCreateRequest;
import com.geunnseung.daengnyangrefactor.petpost.api.dto.response.PetPostCreateResponse;
import com.geunnseung.daengnyangrefactor.petpost.domain.PetPost;
import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFile;
import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFileType;
import com.geunnseung.daengnyangrefactor.petpost.repository.PetPostFileRepository;
import com.geunnseung.daengnyangrefactor.petpost.repository.PetPostRepository;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import com.geunnseung.daengnyangrefactor.user.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PetPostService {

    private static final long MAX_IMAGE_SIZE = 10L * 1024 * 1024;
    private static final long MAX_VIDEO_SIZE = 100L * 1024 * 1024;
    private static final Set<String> IMAGE_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
    );
    private static final Set<String> VIDEO_CONTENT_TYPES = Set.of(
            "video/mp4",
            "video/quicktime",
            "video/webm"
    );

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final PetPostRepository petPostRepository;
    private final PetPostFileRepository petPostFileRepository;
    private final MediaStoragePort mediaStoragePort;

    @Transactional
    public PetPostCreateResponse createPetPost(
            final Long userId,
            final Long petId,
            final PetPostCreateRequest request,
            final MultipartFile file
    ) {
        Pet pet = petRepository.findByIdWithGroup(petId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));
        validateWritable(userId, pet);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.USER_NOT_FOUND));

        PetPostFileType fileType = resolveFileType(file);
        validateFileSize(fileType, file);

        PetPost petPost = PetPost.create(pet, user, request.content());
        petPostRepository.save(petPost);

        MediaUploadResult uploadResult = mediaStoragePort.store(
                new MediaUploadCommand(
                        file,
                        "pet-posts/" + pet.getId() + "/" + petPost.getId()
                )
        );

        PetPostFile petPostFile = PetPostFile.create(
                petPost,
                fileType,
                uploadResult.fileUrl(),
                uploadResult.objectKey(),
                uploadResult.contentType(),
                uploadResult.fileSize()
        );
        petPostFileRepository.save(petPostFile);

        return new PetPostCreateResponse(
                petPost.getId(),
                pet.getId(),
                user.getId(),
                petPost.getContent(),
                fileType,
                uploadResult.fileUrl()
        );
    }

    private void validateWritable(final Long userId, final Pet pet) {
        if (pet.getOwner().getId().equals(userId)) {
            return;
        }

        Group group = pet.getGroup();
        if (group != null && userGroupRepository.existsByUserIdAndGroupId(userId, group.getId())) {
            return;
        }

        throw new DaengnyangException(ErrorCode.PET_NOT_FOUND);
    }

    private PetPostFileType resolveFileType(final MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new DaengnyangException(ErrorCode.INVALID_REQUEST);
        }

        String contentType = file.getContentType();
        if (contentType == null || contentType.isBlank()) {
            throw new DaengnyangException(ErrorCode.UNSUPPORTED_FILE_TYPE);
        }

        if (IMAGE_CONTENT_TYPES.contains(contentType)) {
            return PetPostFileType.IMAGE;
        }

        if (VIDEO_CONTENT_TYPES.contains(contentType)) {
            return PetPostFileType.VIDEO;
        }

        throw new DaengnyangException(ErrorCode.UNSUPPORTED_FILE_TYPE);
    }

    private void validateFileSize(final PetPostFileType fileType, final MultipartFile file) {
        long fileSize = file.getSize();

        if (fileSize <= 0) {
            throw new DaengnyangException(ErrorCode.INVALID_REQUEST);
        }

        long maxSize = fileType == PetPostFileType.IMAGE
                ? MAX_IMAGE_SIZE
                : MAX_VIDEO_SIZE;

        if (fileSize > maxSize) {
            throw new DaengnyangException(ErrorCode.FILE_TOO_LARGE);
        }
    }
}
