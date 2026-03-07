package com.geunnseung.daengnyangrefactor.group.api;

import com.geunnseung.daengnyangrefactor.auth.support.AuthenticatedUser;
import com.geunnseung.daengnyangrefactor.auth.support.LoginUser;
import com.geunnseung.daengnyangrefactor.group.api.dto.response.GroupJoinApplicationCreateResponse;
import com.geunnseung.daengnyangrefactor.group.service.GroupJoinApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class GroupJoinApplicationController {

    private final GroupJoinApplicationService groupJoinApplicationService;

    @PostMapping("/{groupId}/join-applications")
    public ResponseEntity<GroupJoinApplicationCreateResponse> createJoinApplication(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long groupId
    ) {
        GroupJoinApplicationCreateResponse response = groupJoinApplicationService.createJoinApplication(
                authenticatedUser.id(),
                groupId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
