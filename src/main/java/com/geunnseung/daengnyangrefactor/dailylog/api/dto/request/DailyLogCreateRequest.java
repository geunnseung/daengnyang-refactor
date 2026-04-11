package com.geunnseung.daengnyangrefactor.dailylog.api.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyLogCreateRequest(

        @NotNull(message = "기록 날짜는 필수입니다.")
        LocalDate recordDate,

        @DecimalMin(value = "0.0", message = "체중은 0 이상이어야 합니다.")
        @Digits(integer = 3, fraction = 2, message = "체중은 정수 3자리, 소수 2자리까지 입력할 수 있습니다.")
        BigDecimal weightKg,

        @PositiveOrZero(message = "식사량은 0 이상이어야 합니다.")
        Integer mealAmountG,

        @PositiveOrZero(message = "음수량은 0 이상이어야 합니다.")
        Integer waterAmountMl,

        @PositiveOrZero(message = "산책 거리는 0 이상이어야 합니다.")
        Integer walkDistanceM,

        @PositiveOrZero(message = "산책 시간은 0 이상이어야 합니다.")
        Integer walkDurationMinutes,

        @PositiveOrZero(message = "수면 시간은 0 이상이어야 합니다.")
        Integer sleepDurationMinutes,

        @PositiveOrZero(message = "배변 횟수는 0 이상이어야 합니다.")
        Integer stoolCount,

        @PositiveOrZero(message = "배뇨 횟수는 0 이상이어야 합니다.")
        Integer urineCount,

        @PositiveOrZero(message = "구토 횟수는 0 이상이어야 합니다.")
        Integer vomitCount,

        @PositiveOrZero(message = "설사 횟수는 0 이상이어야 합니다.")
        Integer diarrheaCount,

        Boolean medicated,

        Boolean coughing,

        Boolean poorAppetite,

        Boolean lowActivity,

        @Size(max = 500, message = "이상 증상 메모는 최대 500자까지 입력할 수 있습니다.")
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
