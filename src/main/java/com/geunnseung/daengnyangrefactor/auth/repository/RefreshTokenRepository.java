package com.geunnseung.daengnyangrefactor.auth.repository;

import com.geunnseung.daengnyangrefactor.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
