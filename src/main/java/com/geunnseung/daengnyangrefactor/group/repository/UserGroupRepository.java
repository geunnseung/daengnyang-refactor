package com.geunnseung.daengnyangrefactor.group.repository;

import com.geunnseung.daengnyangrefactor.group.domain.UserGroup;
import java.util.List;
import java.util.Optional;
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

    @Query("""
            select userGroup
            from UserGroup userGroup
            join fetch userGroup.group
            where userGroup.user.id = :userId
              and userGroup.group.id = :groupId
            """)
    Optional<UserGroup> findWithGroupByUserIdAndGroupId(
            @Param("userId") final Long userId,
            @Param("groupId") final Long groupId
    );

    boolean existsByUserIdAndGroupId(final Long userId, final Long groupId);

    @Query("""
            select userGroup
            from UserGroup userGroup
            join fetch userGroup.user
            where userGroup.group.id = :groupId
            order by userGroup.createdAt asc
            """)
    List<UserGroup> findAllWithUserByGroupId(@Param("groupId") final Long groupId);
}
