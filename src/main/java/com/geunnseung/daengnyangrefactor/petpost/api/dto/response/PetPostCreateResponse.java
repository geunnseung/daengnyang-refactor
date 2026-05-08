package com.geunnseung.daengnyangrefactor.petpost.api.dto.response;

import com.geunnseung.daengnyangrefactor.petpost.domain.PetPost;
import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFile;
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

    public static PetPostCreateResponse of(
            final PetPost petPost,
            final PetPostFile petPostFile
    ) {
        return new PetPostCreateResponse(
                petPost.getId(),
                petPost.getPet().getId(),
                petPost.getAuthor().getId(),
                petPost.getRecordDate(),
                petPostFile.getFileType(),
                petPostFile.getFileUrl(),
                petPost.getContent()
        );
    }
}
