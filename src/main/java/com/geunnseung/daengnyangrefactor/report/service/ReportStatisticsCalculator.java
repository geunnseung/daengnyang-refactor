package com.geunnseung.daengnyangrefactor.report.service;

import com.geunnseung.daengnyangrefactor.dailylog.domain.DailyLog;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class ReportStatisticsCalculator {

    public ReportStatistics calculate(final List<DailyLog> dailyLogs) {
        return new ReportStatistics(
                dailyLogs.size(),
                average(dailyLogs.stream().map(DailyLog::getWeightKg).toList()),
                averageInteger(dailyLogs.stream().map(DailyLog::getMealAmountG).toList()),
                averageInteger(dailyLogs.stream().map(DailyLog::getWaterAmountMl).toList()),
                sumInteger(dailyLogs.stream().map(DailyLog::getWalkDistanceM).toList()),
                sumInteger(dailyLogs.stream().map(DailyLog::getWalkDurationMinutes).toList()),
                averageInteger(dailyLogs.stream().map(DailyLog::getSleepDurationMinutes).toList()),
                sumInteger(dailyLogs.stream().map(DailyLog::getStoolCount).toList()),
                sumInteger(dailyLogs.stream().map(DailyLog::getUrineCount).toList()),
                sumInteger(dailyLogs.stream().map(DailyLog::getVomitCount).toList()),
                sumInteger(dailyLogs.stream().map(DailyLog::getDiarrheaCount).toList()),
                countTrue(dailyLogs.stream().map(DailyLog::getMedicated).toList()),
                countTrue(dailyLogs.stream().map(DailyLog::getCoughing).toList()),
                countTrue(dailyLogs.stream().map(DailyLog::getPoorAppetite).toList()),
                countTrue(dailyLogs.stream().map(DailyLog::getLowActivity).toList())
        );
    }

    private BigDecimal average(final List<BigDecimal> values) {
        List<BigDecimal> recordedValues = values.stream()
                .filter(Objects::nonNull)
                .toList();

        if (recordedValues.isEmpty()) {
            return null;
        }

        BigDecimal sum = recordedValues.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.divide(BigDecimal.valueOf(recordedValues.size()), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal averageInteger(final List<Integer> values) {
        List<Integer> recordedValues = values.stream()
                .filter(Objects::nonNull)
                .toList();

        if (recordedValues.isEmpty()) {
            return null;
        }

        int sum = recordedValues.stream()
                .mapToInt(Integer::intValue)
                .sum();

        return BigDecimal.valueOf(sum)
                .divide(BigDecimal.valueOf(recordedValues.size()), 2, RoundingMode.HALF_UP);
    }

    private Integer sumInteger(final List<Integer> values) {
        List<Integer> recordedValues = values.stream()
                .filter(Objects::nonNull)
                .toList();

        if (recordedValues.isEmpty()) {
            return null;
        }

        return recordedValues.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private Integer countTrue(final List<Boolean> values) {
        List<Boolean> recordedValues = values.stream()
                .filter(Objects::nonNull)
                .toList();

        if (recordedValues.isEmpty()) {
            return null;
        }

        return (int) recordedValues.stream()
                .filter(Boolean::booleanValue)
                .count();
    }
}
