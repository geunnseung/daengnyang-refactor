package com.geunnseung.daengnyangrefactor.petpost.api.dto.response;

import java.time.LocalDate;
import java.util.List;

public record PetPostDailyResponse(

        LocalDate recordDate,
        List<PetPostDetailResponse> posts
) {

    public static PetPostDailyResponse of(
            final LocalDate recordDate,
            final List<PetPostDetailResponse> posts
    ) {
        return new PetPostDailyResponse(recordDate, posts);
    }
}
