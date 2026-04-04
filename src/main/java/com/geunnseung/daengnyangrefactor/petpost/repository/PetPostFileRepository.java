package com.geunnseung.daengnyangrefactor.petpost.repository;

import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetPostFileRepository extends JpaRepository<PetPostFile, Long> {

    Optional<PetPostFile> findByPetPostId(Long petPostId);
}
