package com.geunnseung.daengnyangrefactor.petpost.api;

import com.geunnseung.daengnyangrefactor.auth.support.AuthenticatedUser;
import com.geunnseung.daengnyangrefactor.auth.support.LoginUser;
import com.geunnseung.daengnyangrefactor.petpost.api.dto.request.PetPostCreateRequest;
import com.geunnseung.daengnyangrefactor.petpost.api.dto.response.PetPostCreateResponse;
import com.geunnseung.daengnyangrefactor.petpost.api.dto.response.PetPostDailyResponse;
import com.geunnseung.daengnyangrefactor.petpost.service.PetPostService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/pets/{petId}/posts")
@RequiredArgsConstructor
public class PetPostController {

    private final PetPostService petPostService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetPostCreateResponse> createPetPost(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId,
            @Valid @RequestPart("request") final PetPostCreateRequest request,
            @RequestPart("file") final MultipartFile file
    ) {
        PetPostCreateResponse response = petPostService.createPetPost(
                authenticatedUser.id(),
                petId,
                request,
                file
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PetPostDailyResponse> getDailyPetPosts(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId,
            @RequestParam final LocalDate recordDate
    ) {
        PetPostDailyResponse response = petPostService.getDailyPetPosts(
                authenticatedUser.id(),
                petId,
                recordDate
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{petPostId}")
    public ResponseEntity<Void> deletePetPost(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId,
            @PathVariable final Long petPostId
    ) {
        petPostService.deletePetPost(
                authenticatedUser.id(),
                petId,
                petPostId
        );

        return ResponseEntity.noContent().build();
    }
}
