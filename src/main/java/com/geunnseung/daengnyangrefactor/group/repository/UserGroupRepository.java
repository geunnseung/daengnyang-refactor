package com.geunnseung.daengnyangrefactor.group.repository;

import com.geunnseung.daengnyangrefactor.group.api.dto.response.MyGroupResponse;
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

    @Query("""
            select new com.geunnseung.daengnyangrefactor.group.api.dto.response.MyGroupResponse(
                group.id,
                group.name,
                group.description,
                userGroup.role,
                pet.id,
                pet.name
            )
            from UserGroup userGroup
            join userGroup.group group
            join Pet pet on pet.group.id = group.id
            where userGroup.user.id = :userId
            order by userGroup.createdAt desc
            """)
    List<MyGroupResponse> findMyGroupsByUserId(@Param("userId") final Long userId);
}
