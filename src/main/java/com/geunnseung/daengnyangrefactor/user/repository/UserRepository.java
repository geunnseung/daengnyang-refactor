package com.geunnseung.daengnyangrefactor.user.repository;

import com.geunnseung.daengnyangrefactor.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
