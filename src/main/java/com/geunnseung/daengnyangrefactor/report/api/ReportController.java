package com.geunnseung.daengnyangrefactor.report.api;

import com.geunnseung.daengnyangrefactor.auth.support.AuthenticatedUser;
import com.geunnseung.daengnyangrefactor.auth.support.LoginUser;
import com.geunnseung.daengnyangrefactor.report.api.dto.response.ReportResponse;
import com.geunnseung.daengnyangrefactor.report.service.ReportService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pets/{petId}/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/weekly")
    public ResponseEntity<ReportResponse> getWeeklyReport(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId,
            @RequestParam final LocalDate date
    ) {
        ReportResponse response = reportService.getWeeklyReport(
                authenticatedUser.id(),
                petId,
                date
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly")
    public ResponseEntity<ReportResponse> getMonthlyReport(
            @LoginUser final AuthenticatedUser authenticatedUser,
            @PathVariable final Long petId,
            @RequestParam final LocalDate date
    ) {
        ReportResponse response = reportService.getMonthlyReport(
                authenticatedUser.id(),
                petId,
                date
        );

        return ResponseEntity.ok(response);
    }
}
