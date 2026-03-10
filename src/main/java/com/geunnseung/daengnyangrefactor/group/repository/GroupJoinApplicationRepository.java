package com.geunnseung.daengnyangrefactor.group.repository;

import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplication;
import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplicationStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupJoinApplicationRepository extends JpaRepository<GroupJoinApplication, Long> {

    boolean existsByGroupIdAndRequesterIdAndStatus(
            final Long groupId,
            final Long requesterId,
            final GroupJoinApplicationStatus status
    );

    @Query("""
            select application
            from GroupJoinApplication application
            join fetch application.requester
            where application.group.id = :groupId
              and application.status = :status
            order by application.createdAt asc
            """)
    List<GroupJoinApplication> findAllWithRequesterByGroupIdAndStatus(
            @Param("groupId") final Long groupId,
            @Param("status") final GroupJoinApplicationStatus status
    );
}
