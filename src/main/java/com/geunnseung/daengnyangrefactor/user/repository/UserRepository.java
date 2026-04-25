package com.geunnseung.daengnyangrefactor.user.repository;

import com.geunnseung.daengnyangrefactor.user.domain.User;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select user
            from User user
            where user.id = :userId
            """)
    Optional<User> findByIdForUpdate(@Param("userId") Long userId);
}
