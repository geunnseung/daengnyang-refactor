package com.geunnseung.daengnyangrefactor.petpost.api.dto.response;

import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFileType;
import java.time.LocalDate;

public record PetPostCreateResponse(

        Long id,
        Long petId,
        Long authorId,
        LocalDate recordDate,
        PetPostFileType fileType,
        String fileUrl,
        String content
) {
}
