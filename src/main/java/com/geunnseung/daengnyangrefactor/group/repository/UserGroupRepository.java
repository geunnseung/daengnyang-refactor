package com.geunnseung.daengnyangrefactor.group.repository;

import com.geunnseung.daengnyangrefactor.group.domain.UserGroup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query("""
            select userGroup
            from UserGroup userGroup
            join fetch userGroup.group
            where userGroup.user.id = :userId
            order by userGroup.createdAt desc
            """)
    List<UserGroup> findAllWithGroupByUserId(@Param("userId") final Long userId);
}
