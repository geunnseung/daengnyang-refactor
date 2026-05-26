package com.geunnseung.daengnyangrefactor.petpost.repository;

import com.geunnseung.daengnyangrefactor.petpost.domain.PetPost;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetPostRepository extends JpaRepository<PetPost, Long> {

    Optional<PetPost> findByIdAndPetIdAndDeletedAtIsNull(Long id, Long petId);

    @Query("""
            select petPost
            from PetPost petPost
            join fetch petPost.pet pet
            join fetch pet.owner
            left join fetch pet.group
            where petPost.id = :petPostId
              and petPost.deletedAt is null
            """)
    Optional<PetPost> findWithPetAndOwnerAndGroupById(
            @Param("petPostId") Long petPostId
    );
}
