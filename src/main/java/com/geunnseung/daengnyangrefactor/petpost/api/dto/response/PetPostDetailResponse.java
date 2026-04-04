package com.geunnseung.daengnyangrefactor.petpost.api.dto.response;

import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFileType;
import java.time.LocalDateTime;

public record PetPostDetailResponse(

        Long id,
        Long authorId,
        String authorNickname,
        PetPostFileType fileType,
        String fileUrl,
        String content,
        LocalDateTime createdAt
) {
}
