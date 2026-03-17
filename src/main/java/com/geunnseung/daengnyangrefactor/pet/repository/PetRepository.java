package com.geunnseung.daengnyangrefactor.pet.repository;

import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findAllByOwnerIdOrderByCreatedAtDesc(final Long ownerId);

    Optional<Pet> findByIdAndOwnerId(final Long petId, final Long ownerId);
}
