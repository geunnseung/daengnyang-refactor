package com.geunnseung.daengnyangrefactor.petpost.repository;

import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFile;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PetPostFileRepository extends JpaRepository<PetPostFile, Long> {

    Optional<PetPostFile> findByPetPostId(Long petPostId);

    @Query("""
            select file
            from PetPostFile file
            join fetch file.petPost petPost
            join fetch petPost.author
            where petPost.pet.id = :petId
              and petPost.recordDate = :recordDate
              and petPost.deletedAt is null
            order by petPost.createdAt asc, petPost.id asc
            """)
    List<PetPostFile> findAllWithPetPostAndAuthorByPetIdAndRecordDate(
            @Param("petId") Long petId,
            @Param("recordDate") LocalDate recordDate
    );
}
