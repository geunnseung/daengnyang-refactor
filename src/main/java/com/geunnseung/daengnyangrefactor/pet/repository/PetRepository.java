package com.geunnseung.daengnyangrefactor.pet.repository;

import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
