package com.geunnseung.daengnyangrefactor.pet.service.command;

public record PetGroupCreateCommand(

        String name,
        String description
) {
}
