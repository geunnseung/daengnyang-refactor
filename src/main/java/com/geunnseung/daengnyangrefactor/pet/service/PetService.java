package com.geunnseung.daengnyangrefactor.pet.service;

import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.group.domain.UserGroup;
import com.geunnseung.daengnyangrefactor.group.repository.GroupRepository;
import com.geunnseung.daengnyangrefactor.group.repository.UserGroupRepository;
import com.geunnseung.daengnyangrefactor.pet.api.dto.request.PetGroupCreateRequest;
import com.geunnseung.daengnyangrefactor.pet.api.dto.request.PetRegisterRequest;
import com.geunnseung.daengnyangrefactor.pet.api.dto.request.PetUpdateRequest;
import com.geunnseung.daengnyangrefactor.pet.api.dto.response.MyPetResponse;
import com.geunnseung.daengnyangrefactor.pet.api.dto.response.PetDetailResponse;
import com.geunnseung.daengnyangrefactor.pet.api.dto.response.PetGroupCreateResponse;
import com.geunnseung.daengnyangrefactor.pet.api.dto.response.PetRegisterResponse;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import com.geunnseung.daengnyangrefactor.pet.repository.PetRepository;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import com.geunnseung.daengnyangrefactor.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;

    @Transactional
    public PetRegisterResponse registerPet(
            final Long userId,
            final PetRegisterRequest request
    ) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.USER_NOT_FOUND));

        Pet pet = Pet.register(
                owner,
                request.name(),
                request.species(),
                request.gender(),
                request.birthDate(),
                request.profileImageUrl()
        );
        petRepository.save(pet);

        return PetRegisterResponse.from(pet);
    }

    public List<MyPetResponse> getMyPets(final Long userId) {
        return petRepository.findAllWithGroupByOwnerId(userId)
                .stream()
                .map(MyPetResponse::from)
                .toList();
    }

    @Transactional
    public PetGroupCreateResponse createPetGroup(
            final Long userId,
            final Long petId,
            final PetGroupCreateRequest request
    ) {
        Pet pet = petRepository.findByIdAndOwnerIdAndDeletedAtIsNull(petId, userId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));

        if (pet.getGroup() != null) {
            throw new DaengnyangException(ErrorCode.PET_ALREADY_HAS_GROUP);
        }

        Group group = Group.create(
                request.name(),
                request.description()
        );
        groupRepository.save(group);

        UserGroup userGroup = UserGroup.createAsOwner(
                pet.getOwner(),
                group
        );
        userGroupRepository.save(userGroup);

        pet.assignGroup(group);

        return PetGroupCreateResponse.of(pet, group);
    }

    public PetDetailResponse getPet(final Long userId, final Long petId) {
        Pet pet = petRepository.findByIdWithGroup(petId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));

        Group group = pet.getGroup();

        boolean isOwner = pet.getOwner().getId().equals(userId);
        boolean isGroupMember = group != null
                && userGroupRepository.existsByUserIdAndGroupId(userId, group.getId());

        if (!isOwner && !isGroupMember) {
            throw new DaengnyangException(ErrorCode.PET_NOT_FOUND);
        }

        return PetDetailResponse.from(pet);
    }

    @Transactional
    public PetDetailResponse updatePetProfile(
            final Long userId,
            final Long petId,
            final PetUpdateRequest request
    ) {
        Pet pet = petRepository.findByIdAndOwnerIdAndDeletedAtIsNull(petId, userId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));

        pet.updateProfile(
                request.name(),
                request.species(),
                request.gender(),
                request.birthDate(),
                request.profileImageUrl()
        );

        return PetDetailResponse.from(pet);
    }

    @Transactional
    public void deletePet(final Long userId, final Long petId) {
        Pet pet = petRepository.findByIdAndOwnerIdAndDeletedAtIsNull(petId, userId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));

        if (pet.getGroup() != null) {
            throw new DaengnyangException(ErrorCode.PET_GROUP_LINKED);
        }

        pet.delete();
    }
}
