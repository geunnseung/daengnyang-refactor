package com.geunnseung.daengnyangrefactor.pet.repository;

import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findAllByOwnerIdOrderByCreatedAtDesc(final Long ownerId);

    Optional<Pet> findByIdAndOwnerId(final Long petId, final Long ownerId);

    @Query("""
            select pet
            from Pet pet
            left join fetch pet.group
            where pet.owner.id = :ownerId
            order by pet.createdAt desc
            """)
    List<Pet> findAllWithGroupByOwnerId(@Param("ownerId") final Long ownerId);

    Optional<Pet> findByGroupId(final Long groupId);
}
