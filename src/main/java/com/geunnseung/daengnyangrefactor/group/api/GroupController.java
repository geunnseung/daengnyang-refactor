package com.geunnseung.daengnyangrefactor.group.api;

import com.geunnseung.daengnyangrefactor.auth.support.AuthenticatedUser;
import com.geunnseung.daengnyangrefactor.auth.support.LoginUser;
import com.geunnseung.daengnyangrefactor.group.api.dto.request.GroupCreateRequest;
import com.geunnseung.daengnyangrefactor.group.api.dto.response.GroupCreateResponse;
import com.geunnseung.daengnyangrefactor.group.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupCreateResponse> createGroup(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @Valid @RequestBody final GroupCreateRequest request
    ) {
        GroupCreateResponse response = groupService.createGroup(
                authenticatedUser.id(),
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
