package com.geunnseung.daengnyangrefactor.pet.repository;

import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetRepository extends JpaRepository<Pet, Long> {

    List<Pet> findAllByOwnerIdAndDeletedAtIsNullOrderByCreatedAtDesc(final Long ownerId);

    Optional<Pet> findByIdAndOwnerIdAndDeletedAtIsNull(final Long petId, final Long ownerId);

    @Query("""
            select pet
            from Pet pet
            left join fetch pet.group
            where pet.owner.id = :ownerId
              and pet.deletedAt is null
            order by pet.createdAt desc
            """)
    List<Pet> findAllWithGroupByOwnerId(@Param("ownerId") final Long ownerId);

    Optional<Pet> findByGroupIdAndDeletedAtIsNull(final Long groupId);

    @Query("""
            select pet
            from Pet pet
            left join fetch pet.group
            where pet.id = :petId
              and pet.deletedAt is null
            """)
    Optional<Pet> findByIdWithGroup(@Param("petId") final Long petId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select pet
            from Pet pet
            left join fetch pet.group
            where pet.id = :petId
              and pet.owner.id = :ownerId
              and pet.deletedAt is null
            """)
    Optional<Pet> findOwnedPetForUpdate(
            @Param("ownerId") final Long ownerId,
            @Param("petId") final Long petId
    );
}
