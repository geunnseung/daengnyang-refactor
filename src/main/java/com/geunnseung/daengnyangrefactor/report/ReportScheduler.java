package com.geunnseung.daengnyangrefactor.report;

import com.geunnseung.daengnyangrefactor.report.service.ReportService;
import java.time.LocalDate;
import java.time.ZoneId;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportScheduler {

    private static final ZoneId REPORT_ZONE = ZoneId.of("Asia/Seoul");

    private final ReportService reportService;

    @Scheduled(cron = "0 0 0 * * MON", zone = "Asia/Seoul")
    public void generateWeeklyReports() {
        reportService.generateWeeklyReports(LocalDate.now(REPORT_ZONE));
    }

    @Scheduled(cron = "0 0 1 1 * *", zone = "Asia/Seoul")
    public void generateMonthlyReports() {
        reportService.generateMonthlyReports(LocalDate.now(REPORT_ZONE));
    }
}
