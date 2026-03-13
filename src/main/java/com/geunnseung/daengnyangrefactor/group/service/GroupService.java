package com.geunnseung.daengnyangrefactor.group.service;

import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.group.api.dto.request.GroupCreateRequest;
import com.geunnseung.daengnyangrefactor.group.api.dto.response.GroupCreateResponse;
import com.geunnseung.daengnyangrefactor.group.api.dto.response.GroupDetailResponse;
import com.geunnseung.daengnyangrefactor.group.api.dto.response.GroupMemberResponse;
import com.geunnseung.daengnyangrefactor.group.api.dto.response.MyGroupResponse;
import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.group.domain.UserGroup;
import com.geunnseung.daengnyangrefactor.group.repository.GroupRepository;
import com.geunnseung.daengnyangrefactor.group.repository.UserGroupRepository;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import com.geunnseung.daengnyangrefactor.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;

    @Transactional
    public GroupCreateResponse createGroup(
            final Long userId,
            final GroupCreateRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.USER_NOT_FOUND));

        Group group = Group.create(
                request.name(),
                request.description()
        );
        groupRepository.save(group);

        UserGroup userGroup = UserGroup.createAsOwner(
                user,
                group
        );
        userGroupRepository.save(userGroup);

        groupRepository.save(group);

        return new GroupCreateResponse(
                group.getId(),
                group.getName()
        );
    }

    public List<MyGroupResponse> getMyGroups(final Long userId) {
        return userGroupRepository.findAllWithGroupByUserId((userId))
                .stream()
                .map(userGroup -> {
                    Group group = userGroup.getGroup();

                    return new MyGroupResponse(
                            group.getId(),
                            group.getName(),
                            group.getDescription(),
                            userGroup.getRole()
                    );
                })
                .toList();
    }

    public GroupDetailResponse getGroup(
            final Long userId,
            final Long groupId
    ) {
        UserGroup userGroup = userGroupRepository.findWithGroupByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.GROUP_NOT_FOUND));

        Group group = userGroup.getGroup();

        return new GroupDetailResponse(
                group.getId(),
                group.getName(),
                group.getDescription(),
                userGroup.getRole()
        );
    }

    public List<GroupMemberResponse> getGroupMembers(
            final Long userId,
            final Long groupId
    ) {
        userGroupRepository.findWithGroupByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.GROUP_NOT_FOUND));

        return userGroupRepository.findAllWithUserByGroupId(groupId)
                .stream()
                .map(userGroup -> new GroupMemberResponse(
                        userGroup.getUser().getId(),
                        userGroup.getUser().getNickname(),
                        userGroup.getRole(),
                        userGroup.getCreatedAt()
                ))
                .toList();
    }
}
