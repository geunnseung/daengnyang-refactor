package com.geunnseung.daengnyangrefactor.petpost.service.cache;

import java.time.LocalDate;

public record PetPostDailyCacheRefreshEvent(

        Long petId,
        LocalDate recordDate
) {
}
