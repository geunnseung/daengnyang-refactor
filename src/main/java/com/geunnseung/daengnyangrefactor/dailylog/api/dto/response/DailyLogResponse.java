package com.geunnseung.daengnyangrefactor.dailylog.api.dto.response;

import com.geunnseung.daengnyangrefactor.dailylog.domain.DailyLog;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DailyLogResponse(

        Long id,
        Long petId,
        LocalDate recordDate,
        BigDecimal weightKg,
        Integer mealAmountG,
        Integer waterAmountMl,
        Integer walkDistanceM,
        Integer walkDurationMinutes,
        Integer sleepDurationMinutes,
        Integer stoolCount,
        Integer urineCount,
        Integer vomitCount,
        Integer diarrheaCount,
        Boolean medicated,
        Boolean coughing,
        Boolean poorAppetite,
        Boolean lowActivity,
        String abnormalNote,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static DailyLogResponse from(final DailyLog dailyLog) {
        return new DailyLogResponse(
                dailyLog.getId(),
                dailyLog.getPet().getId(),
                dailyLog.getRecordDate(),
                dailyLog.getWeightKg(),
                dailyLog.getMealAmountG(),
                dailyLog.getWaterAmountMl(),
                dailyLog.getWalkDistanceM(),
                dailyLog.getWalkDurationMinutes(),
                dailyLog.getSleepDurationMinutes(),
                dailyLog.getStoolCount(),
                dailyLog.getUrineCount(),
                dailyLog.getVomitCount(),
                dailyLog.getDiarrheaCount(),
                dailyLog.getMedicated(),
                dailyLog.getCoughing(),
                dailyLog.getPoorAppetite(),
                dailyLog.getLowActivity(),
                dailyLog.getAbnormalNote(),
                dailyLog.getCreatedAt(),
                dailyLog.getUpdatedAt()
        );
    }
}
