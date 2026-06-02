package com.geunnseung.daengnyangrefactor.report.repository;

import java.math.BigDecimal;

public record ReportStatisticsResult(

        Long petId,
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
        Integer lowActivityDays
) {
}
