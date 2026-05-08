package com.geunnseung.daengnyangrefactor.petpost.api.dto.response;

import com.geunnseung.daengnyangrefactor.petpost.domain.PetPost;
import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFile;
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

    public static PetPostDetailResponse of(
            final PetPost petPost,
            final PetPostFile file
    ) {
        return new PetPostDetailResponse(
                petPost.getId(),
                petPost.getAuthor().getId(),
                petPost.getAuthor().getNickname(),
                file.getFileType(),
                file.getFileUrl(),
                petPost.getContent(),
                petPost.getCreatedAt()
        );
    }
}
