package com.geunnseung.daengnyangrefactor.petpost.api.dto.response;

import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFileType;

public record PetPostCreateResponse(

        Long id,
        Long petId,
        Long authorId,
        String content,
        PetPostFileType fileType,
        String fileUrl
) {
}
