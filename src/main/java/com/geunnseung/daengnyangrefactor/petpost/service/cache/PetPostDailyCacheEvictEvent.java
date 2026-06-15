package com.geunnseung.daengnyangrefactor.petpost.service.cache;

import java.time.LocalDate;

public record PetPostDailyCacheEvictEvent(

        Long petId,
        LocalDate recordDate
) {
}
