package com.geunnseung.daengnyangrefactor.petpost.repository;

import com.geunnseung.daengnyangrefactor.petpost.domain.PetPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetPostRepository extends JpaRepository<PetPost, Long> {
}
