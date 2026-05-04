package com.geunnseung.daengnyangrefactor.pet.api;

import com.geunnseung.daengnyangrefactor.auth.support.AuthenticatedUser;
import com.geunnseung.daengnyangrefactor.auth.support.LoginUser;
import com.geunnseung.daengnyangrefactor.pet.api.dto.request.PetGroupCreateRequest;
import com.geunnseung.daengnyangrefactor.pet.api.dto.request.PetRegisterRequest;
import com.geunnseung.daengnyangrefactor.pet.api.dto.request.PetUpdateRequest;
import com.geunnseung.daengnyangrefactor.pet.api.dto.response.MyPetResponse;
import com.geunnseung.daengnyangrefactor.pet.api.dto.response.PetDetailResponse;
import com.geunnseung.daengnyangrefactor.pet.api.dto.response.PetGroupCreateResponse;
import com.geunnseung.daengnyangrefactor.pet.api.dto.response.PetRegisterResponse;
import com.geunnseung.daengnyangrefactor.pet.service.PetService;
import com.geunnseung.daengnyangrefactor.pet.service.command.PetGroupCreateCommand;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<PetRegisterResponse> registerPet(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @Valid @RequestBody final PetRegisterRequest request
    ) {
        PetRegisterResponse response = petService.registerPet(
                authenticatedUser.id(),
                request.toCommand()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{petId}")
    public ResponseEntity<PetDetailResponse> getPet(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId
    ) {
        PetDetailResponse response = petService.getPet(
                authenticatedUser.id(),
                petId
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MyPetResponse>> getMyPets(
            @LoginUser final AuthenticatedUser authenticatedUser
    ) {
        List<MyPetResponse> responses = petService.getMyPets(authenticatedUser.id());

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{petId}/group")
    public ResponseEntity<PetGroupCreateResponse> createPetGroup(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId,
            @Valid @RequestBody final PetGroupCreateRequest request
    ) {
        PetGroupCreateResponse response = petService.createPetGroup(
                authenticatedUser.id(),
                petId,
                request.toCommand()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{petId}")
    public ResponseEntity<PetDetailResponse> updatePetProfile(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId,
            @Valid @RequestBody final PetUpdateRequest request
    ) {
        PetDetailResponse response = petService.updatePetProfile(
                authenticatedUser.id(),
                petId,
                request.toCommand()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> deletePet(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId
    ) {
        petService.deletePet(authenticatedUser.id(), petId);

        return ResponseEntity.noContent().build();
    }
}
