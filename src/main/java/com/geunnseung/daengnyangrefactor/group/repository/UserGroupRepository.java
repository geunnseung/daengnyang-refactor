package com.geunnseung.daengnyangrefactor.group.repository;

import com.geunnseung.daengnyangrefactor.group.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
}
