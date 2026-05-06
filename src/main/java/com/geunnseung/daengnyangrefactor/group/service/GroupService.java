package com.geunnseung.daengnyangrefactor.group.service;

import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.group.api.dto.response.GroupCreateResponse;
import com.geunnseung.daengnyangrefactor.group.api.dto.response.GroupDetailResponse;
import com.geunnseung.daengnyangrefactor.group.api.dto.response.GroupMemberResponse;
import com.geunnseung.daengnyangrefactor.group.api.dto.response.MyGroupResponse;
import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.group.domain.UserGroup;
import com.geunnseung.daengnyangrefactor.group.repository.GroupRepository;
import com.geunnseung.daengnyangrefactor.group.repository.UserGroupRepository;
import com.geunnseung.daengnyangrefactor.group.service.command.GroupCreateCommand;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import com.geunnseung.daengnyangrefactor.pet.repository.PetRepository;
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
    private final PetRepository petRepository;

    @Transactional
    public GroupCreateResponse createGroup(final Long userId, final GroupCreateCommand command) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.USER_NOT_FOUND));

        Group group = Group.create(
                command.name(),
                command.description()
        );
        groupRepository.save(group);

        UserGroup userGroup = UserGroup.createAsOwner(
                user,
                group
        );
        userGroupRepository.save(userGroup);

        return GroupCreateResponse.from(group);
    }

    public List<MyGroupResponse> getMyGroups(final Long userId) {
        return userGroupRepository.findMyGroupsByUserId(userId);
    }

    public GroupDetailResponse getGroup(final Long userId, final Long groupId) {
        UserGroup userGroup = userGroupRepository.findWithGroupByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.GROUP_NOT_FOUND));

        Group group = userGroup.getGroup();

        Pet pet = petRepository.findByGroupIdAndDeletedAtIsNull(group.getId())
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));

        return GroupDetailResponse.of(userGroup, pet);
    }

    public List<GroupMemberResponse> getGroupMembers(final Long userId, final Long groupId) {
        userGroupRepository.findWithGroupByUserIdAndGroupId(userId, groupId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.GROUP_NOT_FOUND));

        return userGroupRepository.findAllWithUserByGroupId(groupId)
                .stream()
                .map(GroupMemberResponse::from)
                .toList();
    }
}
