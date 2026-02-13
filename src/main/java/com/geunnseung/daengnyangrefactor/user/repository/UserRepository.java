package com.geunnseung.daengnyangrefactor.user.repository;

import com.geunnseung.daengnyangrefactor.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
