package com.geunnseung.daengnyangrefactor.report.api.dto.response;

import com.geunnseung.daengnyangrefactor.report.domain.Report;
import com.geunnseung.daengnyangrefactor.report.domain.ReportType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReportResponse(

        Long id,
        Long petId,
        ReportType type,
        LocalDate periodStart,
        LocalDate periodEnd,
        Integer recordedDays,
        BigDecimal averageWeightKg,
        BigDecimal averageMealAmountG,
        BigDecimal averageWaterAmountMl,
        Integer totalWalkDistanceM,
        Integer totalWalkDurationMinutes,
        BigDecimal averageSleepDurationMinutes,
        Integer totalStoolCount,
        Integer totalUrineCount,
        Integer totalVomitCount,
        Integer totalDiarrheaCount,
        Integer medicatedDays,
        Integer coughingDays,
        Integer poorAppetiteDays,
        Integer lowActivityDays,
        LocalDateTime createdAt
) {

    public static ReportResponse of(
            final Report report,
            final Long petId
    ) {
        return new ReportResponse(
                report.getId(),
                petId,
                report.getType(),
                report.getPeriodStart(),
                report.getPeriodEnd(),
                report.getRecordedDays(),
                report.getAverageWeightKg(),
                report.getAverageMealAmountG(),
                report.getAverageWaterAmountMl(),
                report.getTotalWalkDistanceM(),
                report.getTotalWalkDurationMinutes(),
                report.getAverageSleepDurationMinutes(),
                report.getTotalStoolCount(),
                report.getTotalUrineCount(),
                report.getTotalVomitCount(),
                report.getTotalDiarrheaCount(),
                report.getMedicatedDays(),
                report.getCoughingDays(),
                report.getPoorAppetiteDays(),
                report.getLowActivityDays(),
                report.getCreatedAt()
        );
    }
}
