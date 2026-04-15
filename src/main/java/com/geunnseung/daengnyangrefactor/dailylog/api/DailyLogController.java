package com.geunnseung.daengnyangrefactor.dailylog.api;

import com.geunnseung.daengnyangrefactor.auth.support.AuthenticatedUser;
import com.geunnseung.daengnyangrefactor.auth.support.LoginUser;
import com.geunnseung.daengnyangrefactor.dailylog.api.dto.request.DailyLogCreateRequest;
import com.geunnseung.daengnyangrefactor.dailylog.api.dto.request.DailyLogUpdateRequest;
import com.geunnseung.daengnyangrefactor.dailylog.api.dto.response.DailyLogResponse;
import com.geunnseung.daengnyangrefactor.dailylog.service.DailyLogService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public ResponseEntity<List<DailyLogResponse>> getDailyLogsInPeriod(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId,
            @RequestParam final LocalDate from,
            @RequestParam final LocalDate to
    ) {
        List<DailyLogResponse> responses = dailyLogService.getDailyLogsInPeriod(
                authenticatedUser.id(),
                petId,
                from,
                to
        );

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{recordDate}")
    public ResponseEntity<DailyLogResponse> getDailyLog(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId,
            @PathVariable final LocalDate recordDate
    ) {
        DailyLogResponse response = dailyLogService.getDailyLog(
                authenticatedUser.id(),
                petId,
                recordDate
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{recordDate}")
    public ResponseEntity<DailyLogResponse> updateDailyLog(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId,
            @PathVariable final LocalDate recordDate,
            @Valid @RequestBody final DailyLogUpdateRequest request
    ) {
        DailyLogResponse response = dailyLogService.updateDailyLog(
                authenticatedUser.id(),
                petId,
                recordDate,
                request
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{recordDate}")
    public ResponseEntity<Void> deleteDailyLog(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId,
            @PathVariable final LocalDate recordDate
    ) {
        dailyLogService.deleteDailyLog(
                authenticatedUser.id(),
                petId,
                recordDate
        );

        return ResponseEntity.noContent().build();
    }
}
