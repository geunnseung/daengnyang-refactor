package com.geunnseung.daengnyangrefactor.group.repository;

import com.geunnseung.daengnyangrefactor.group.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}
