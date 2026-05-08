package com.geunnseung.daengnyangrefactor.petpost.service.command;

import java.time.LocalDate;

public record PetPostCreateCommand(

        LocalDate recordDate,
        String content
) {
}
