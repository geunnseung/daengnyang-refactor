package com.geunnseung.daengnyangrefactor.petpost.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record PetPostCreateRequest(

        @NotNull(message = "기록 날짜는 필수입니다.")
        LocalDate recordDate,

        @Size(max = 1000, message = "게시글 내용은 최대 1000자까지 입력할 수 있습니다.")
        String content
) {
}
