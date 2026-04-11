package com.geunnseung.daengnyangrefactor.dailylog.api;

import com.geunnseung.daengnyangrefactor.auth.support.AuthenticatedUser;
import com.geunnseung.daengnyangrefactor.auth.support.LoginUser;
import com.geunnseung.daengnyangrefactor.dailylog.api.dto.request.DailyLogCreateRequest;
import com.geunnseung.daengnyangrefactor.dailylog.api.dto.response.DailyLogResponse;
import com.geunnseung.daengnyangrefactor.dailylog.service.DailyLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pets/{petId}/daily-logs")
@RequiredArgsConstructor
public class DailyLogController {

    private final DailyLogService dailyLogService;

    @PostMapping
    public ResponseEntity<DailyLogResponse> createDailyLog(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId,
            @Valid @RequestBody final DailyLogCreateRequest request
    ) {
        DailyLogResponse response = dailyLogService.createDailyLog(
                authenticatedUser.id(),
                petId,
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
