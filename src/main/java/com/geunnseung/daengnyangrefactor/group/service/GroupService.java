package com.geunnseung.daengnyangrefactor.group.service;

import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.group.api.dto.request.GroupCreateRequest;
import com.geunnseung.daengnyangrefactor.group.api.dto.response.GroupCreateResponse;
import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.group.domain.UserGroup;
import com.geunnseung.daengnyangrefactor.group.repository.GroupRepository;
import com.geunnseung.daengnyangrefactor.group.repository.UserGroupRepository;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import com.geunnseung.daengnyangrefactor.user.repository.UserRepository;
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

        UserGroup userGroup = UserGroup.createOwner(
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
}
