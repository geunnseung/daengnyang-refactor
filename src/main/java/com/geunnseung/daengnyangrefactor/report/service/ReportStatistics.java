package com.geunnseung.daengnyangrefactor.report.service;

import java.math.BigDecimal;

record ReportStatistics(

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
