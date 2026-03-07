package com.geunnseung.daengnyangrefactor.group.repository;

import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplication;
import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupJoinApplicationRepository extends JpaRepository<GroupJoinApplication, Long> {

    boolean existsByGroupIdAndRequesterIdAndStatus(
            final Long groupId,
            final Long requesterId,
            final GroupJoinApplicationStatus status
    );
}
