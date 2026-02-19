package com.geunnseung.daengnyangrefactor.auth.repository;

import com.geunnseung.daengnyangrefactor.auth.domain.RefreshToken;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    List<RefreshToken> findAllByUserAndRevokedFalse(User user);
}
