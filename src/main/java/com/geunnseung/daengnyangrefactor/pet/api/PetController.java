package com.geunnseung.daengnyangrefactor.pet.api;

import com.geunnseung.daengnyangrefactor.auth.support.AuthenticatedUser;
import com.geunnseung.daengnyangrefactor.auth.support.LoginUser;
import com.geunnseung.daengnyangrefactor.pet.api.dto.request.PetRegisterRequest;
import com.geunnseung.daengnyangrefactor.pet.api.dto.response.PetRegisterResponse;
import com.geunnseung.daengnyangrefactor.pet.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
