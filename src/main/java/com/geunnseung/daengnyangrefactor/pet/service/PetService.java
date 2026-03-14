package com.geunnseung.daengnyangrefactor.pet.service;

import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.pet.api.dto.request.PetRegisterRequest;
import com.geunnseung.daengnyangrefactor.pet.api.dto.response.PetRegisterResponse;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import com.geunnseung.daengnyangrefactor.pet.repository.PetRepository;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import com.geunnseung.daengnyangrefactor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PetService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

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
}