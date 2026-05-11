package com.geunnseung.daengnyangrefactor.dailylog.service.command;

import java.math.BigDecimal;

public record DailyLogUpdateCommand(

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
        String abnormalNote
) {

    public boolean hasAnyValue() {
        return weightKg != null
                || mealAmountG != null
                || waterAmountMl != null
                || walkDistanceM != null
                || walkDurationMinutes != null
                || sleepDurationMinutes != null
                || stoolCount != null
                || urineCount != null
                || vomitCount != null
                || diarrheaCount != null
                || medicated != null
                || coughing != null
                || poorAppetite != null
                || lowActivity != null
                || (abnormalNote != null && !abnormalNote.isBlank());
    }
}
