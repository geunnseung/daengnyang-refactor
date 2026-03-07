package com.geunnseung.daengnyangrefactor.group.service;

import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.group.api.dto.response.GroupJoinApplicationCreateResponse;
import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplication;
import com.geunnseung.daengnyangrefactor.group.domain.GroupJoinApplicationStatus;
import com.geunnseung.daengnyangrefactor.group.repository.GroupJoinApplicationRepository;
import com.geunnseung.daengnyangrefactor.group.repository.GroupRepository;
import com.geunnseung.daengnyangrefactor.group.repository.UserGroupRepository;
import com.geunnseung.daengnyangrefactor.user.domain.User;
import com.geunnseung.daengnyangrefactor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupJoinApplicationService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupJoinApplicationRepository groupJoinApplicationRepository;

    @Transactional
    public GroupJoinApplicationCreateResponse createJoinApplication(
            final Long requesterId,
            final Long groupId
    ) {
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.USER_NOT_FOUND));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.GROUP_NOT_FOUND));

        if (userGroupRepository.existsByUserIdAndGroupId(requesterId, groupId)) {
            throw new DaengnyangException(ErrorCode.ALREADY_GROUP_MEMBER);
        }

        if (groupJoinApplicationRepository.existsByGroupIdAndRequesterIdAndStatus(
                groupId,
                requesterId,
                GroupJoinApplicationStatus.PENDING
        )) {
            throw new DaengnyangException(ErrorCode.GROUP_JOIN_APPLICATION_ALREADY_EXISTS);
        }

        GroupJoinApplication application = GroupJoinApplication.create(
                group,
                requester
        );
        groupJoinApplicationRepository.save(application);

        return new GroupJoinApplicationCreateResponse(
                application.getId(),
                group.getId(),
                application.getStatus()
        );
    }
}
