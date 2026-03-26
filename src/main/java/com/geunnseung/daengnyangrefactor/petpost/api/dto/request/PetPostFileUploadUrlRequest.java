package com.geunnseung.daengnyangrefactor.petpost.api.dto.request;

import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFileType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PetPostFileUploadUrlRequest(

        @NotNull(message = "파일 타입은 필수입니다.")
        PetPostFileType fileType,

        @NotBlank(message = "콘텐츠 타입은 필수입니다.")
        String contentType,

        @NotNull(message = "파일 크기는 필수입니다.")
        Long fileSize
) {
}
