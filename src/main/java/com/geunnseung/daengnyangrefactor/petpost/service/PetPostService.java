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
import com.geunnseung.daengnyangrefactor.petpost.api.dto.response.PetPostCreateResponse;
import com.geunnseung.daengnyangrefactor.petpost.api.dto.response.PetPostDailyResponse;
import com.geunnseung.daengnyangrefactor.petpost.api.dto.response.PetPostDetailResponse;
import com.geunnseung.daengnyangrefactor.petpost.domain.PetPost;
import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFile;
import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFileType;
import com.geunnseung.daengnyangrefactor.petpost.repository.PetPostFileRepository;
import com.geunnseung.daengnyangrefactor.petpost.repository.PetPostRepository;
import com.geunnseung.daengnyangrefactor.petpost.service.cache.PetPostDailyCache;
import com.geunnseung.daengnyangrefactor.petpost.service.cache.PetPostDailyCacheEvictEvent;
import com.geunnseung.daengnyangrefactor.petpost.service.cache.PetPostDailyCacheRefreshEvent;
import com.geunnseung.daengnyangrefactor.petpost.service.command.PetPostCreateCommand;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import com.geunnseung.daengnyangrefactor.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final PetPostDailyCache petPostDailyCache;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public PetPostCreateResponse createPetPost(
            final Long userId,
            final Long petId,
            final PetPostCreateCommand command,
            final MultipartFile file
    ) {
        Pet pet = findAccessiblePet(userId, petId);

        User user = findUser(userId);

        PetPostFileType fileType = resolveFileType(file);
        validateFileSize(fileType, file);

        PetPost petPost = PetPost.create(pet, user, command.recordDate(), command.content());
        petPostRepository.save(petPost);

        MediaUploadResult uploadResult = uploadPetPostFile(pet, petPost, file);

        PetPostFile petPostFile = PetPostFile.create(
                petPost,
                fileType,
                uploadResult.fileUrl(),
                uploadResult.objectKey(),
                uploadResult.contentType(),
                uploadResult.fileSize()
        );
        petPostFileRepository.save(petPostFile);

        publishTodayPostsCacheRefresh(pet.getId(), command.recordDate());

        return PetPostCreateResponse.of(petPost, petPostFile);
    }

    @Transactional(readOnly = true)
    public PetPostDailyResponse getDailyPetPosts(
            final Long userId,
            final Long petId,
            final LocalDate recordDate
    ) {
        findAccessiblePet(userId, petId);

        if (isToday(recordDate)) {
            return petPostDailyCache.find(petId, recordDate)
                    .orElseGet(() -> loadTodayPosts(petId, recordDate));
        }

        return loadPosts(petId, recordDate);
    }

    @Transactional
    public void deletePetPost(
            final Long userId,
            final Long petId,
            final Long petPostId
    ) {
        PetPost petPost = petPostRepository.findByIdAndPetIdAndDeletedAtIsNull(
                        petPostId,
                        petId
                )
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_POST_NOT_FOUND));

        validateDeletable(userId, petPost);
        petPost.delete();
        publishTodayPostsCacheEviction(petId, petPost.getRecordDate());
    }

    private Pet findAccessiblePet(final Long userId, final Long petId) {
        Pet pet = petRepository.findByIdWithGroup(petId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));

        validatePetAccessible(userId, pet);

        return pet;
    }

    private void validatePetAccessible(final Long userId, final Pet pet) {
        if (pet.getOwner().getId().equals(userId)) {
            return;
        }

        Group group = pet.getGroup();
        if (group != null && userGroupRepository.existsByUserIdAndGroupId(userId, group.getId())) {
            return;
        }

        throw new DaengnyangException(ErrorCode.PET_NOT_FOUND);
    }

    private User findUser(final Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.USER_NOT_FOUND));
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

    private MediaUploadResult uploadPetPostFile(
            final Pet pet,
            final PetPost petPost,
            final MultipartFile file
    ) {
        return mediaStoragePort.store(
                new MediaUploadCommand(
                        file,
                        "pet-posts/" + pet.getId() + "/" + petPost.getId()
                )
        );
    }

    private void validateDeletable(final Long userId, final PetPost petPost) {
        if (petPost.getAuthor().getId().equals(userId)) {
            return;
        }

        if (petPost.getPet().getOwner().getId().equals(userId)) {
            return;
        }

        throw new DaengnyangException(ErrorCode.PET_POST_ACCESS_DENIED);
    }

    private PetPostDailyResponse loadTodayPosts(
            final Long petId,
            final LocalDate recordDate
    ) {
        PetPostDailyResponse response = loadPosts(petId, recordDate);
        petPostDailyCache.put(petId, recordDate, response);

        return response;
    }

    private PetPostDailyResponse loadPosts(
            final Long petId,
            final LocalDate recordDate
    ) {
        List<PetPostFile> files = petPostFileRepository.findAllWithPetPostAndAuthorByPetIdAndRecordDate(
                petId,
                recordDate
        );

        List<PetPostDetailResponse> posts = files.stream()
                .map(file -> PetPostDetailResponse.of(file.getPetPost(), file))
                .toList();

        return PetPostDailyResponse.of(recordDate, posts);
    }

    private void publishTodayPostsCacheRefresh(
            final Long petId,
            final LocalDate recordDate
    ) {
        if (!isToday(recordDate)) {
            return;
        }

        eventPublisher.publishEvent(new PetPostDailyCacheRefreshEvent(petId, recordDate));
    }

    private void publishTodayPostsCacheEviction(
            final Long petId,
            final LocalDate recordDate
    ) {
        if (!isToday(recordDate)) {
            return;
        }

        eventPublisher.publishEvent(new PetPostDailyCacheEvictEvent(petId, recordDate));
    }

    private boolean isToday(final LocalDate recordDate) {
        return LocalDate.now().equals(recordDate);
    }
}
