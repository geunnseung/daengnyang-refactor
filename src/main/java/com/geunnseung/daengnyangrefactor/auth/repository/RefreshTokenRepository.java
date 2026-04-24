package com.geunnseung.daengnyangrefactor.auth.repository;

import com.geunnseung.daengnyangrefactor.auth.domain.RefreshToken;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    List<RefreshToken> findAllByUserAndRevokedFalse(User user);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select refreshToken
            from RefreshToken refreshToken
            join fetch refreshToken.user
            where refreshToken.token = :token
            """)
    Optional<RefreshToken> findByTokenForUpdate(@Param("token") String token);
}
