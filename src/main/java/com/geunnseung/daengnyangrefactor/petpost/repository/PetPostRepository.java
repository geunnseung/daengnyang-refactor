package com.geunnseung.daengnyangrefactor.petpost.repository;

import com.geunnseung.daengnyangrefactor.petpost.domain.PetPost;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetPostRepository extends JpaRepository<PetPost, Long> {

    Optional<PetPost> findByIdAndPetIdAndDeletedAtIsNull(Long id, Long petId);

    Optional<PetPost> findByIdAndDeletedAtIsNull(Long id);
}
