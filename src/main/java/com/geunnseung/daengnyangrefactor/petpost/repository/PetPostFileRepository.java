package com.geunnseung.daengnyangrefactor.petpost.repository;

import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetPostFileRepository extends JpaRepository<PetPostFile, Long> {
}
