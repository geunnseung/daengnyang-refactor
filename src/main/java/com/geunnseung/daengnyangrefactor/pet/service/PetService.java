package com.geunnseung.daengnyangrefactor.pet.service;

import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.group.domain.UserGroup;
import com.geunnseung.daengnyangrefactor.group.repository.GroupRepository;
import com.geunnseung.daengnyangrefactor.group.repository.UserGroupRepository;
import com.geunnseung.daengnyangrefactor.pet.api.dto.request.PetGroupCreateRequest;
import com.geunnseung.daengnyangrefactor.pet.api.dto.request.PetRegisterRequest;
import com.geunnseung.daengnyangrefactor.pet.api.dto.response.MyPetResponse;
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

        return new PetRegisterResponse(
                pet.getId(),
                pet.getName()
        );
    }

    public List<MyPetResponse> getMyPets(final Long userId) {
        return petRepository.findAllByOwnerIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(pet -> new MyPetResponse(
                        pet.getId(),
                        pet.getName(),
                        pet.getSpecies(),
                        pet.getGender(),
                        pet.getBirthDate(),
                        pet.getProfileImageUrl()
                ))
                .toList();
    }

    @Transactional
    public PetGroupCreateResponse createPetGroup(
            final Long userId,
            final Long petId,
            final PetGroupCreateRequest request
    ) {
        Pet pet = petRepository.findByIdAndOwnerId(petId, userId)
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

        return new PetGroupCreateResponse(
                pet.getId(),
                group.getId(),
                group.getName()
        );
    }
}