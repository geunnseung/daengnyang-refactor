package com.geunnseung.daengnyangrefactor.dailylog.domain;

import com.geunnseung.daengnyangrefactor.global.domain.BaseTimeEntity;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "daily_logs",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_daily_log_pet_id_record_date",
                        columnNames = {"pet_id", "record_date"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DailyLog extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "weight_kg", precision = 5, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "meal_amount_g")
    private Integer mealAmountG;

    @Column(name = "water_amount_ml")
    private Integer waterAmountMl;

    @Column(name = "walk_distance_m")
    private Integer walkDistanceM;

    @Column(name = "walk_duration_minutes")
    private Integer walkDurationMinutes;

    @Column(name = "sleep_duration_minutes")
    private Integer sleepDurationMinutes;

    @Column(name = "stool_count")
    private Integer stoolCount;

    @Column(name = "urine_count")
    private Integer urineCount;

    @Column(name = "vomit_count")
    private Integer vomitCount;

    @Column(name = "diarrhea_count")
    private Integer diarrheaCount;

    @Column
    private Boolean medicated;

    @Column
    private Boolean coughing;

    @Column(name = "poor_appetite")
    private Boolean poorAppetite;

    @Column(name = "low_activity")
    private Boolean lowActivity;

    @Column(name = "abnormal_note", length = 500)
    private String abnormalNote;

    public static DailyLog create(
            final Pet pet,
            final LocalDate recordDate,
            final BigDecimal weightKg,
            final Integer mealAmountG,
            final Integer waterAmountMl,
            final Integer walkDistanceM,
            final Integer walkDurationMinutes,
            final Integer sleepDurationMinutes,
            final Integer stoolCount,
            final Integer urineCount,
            final Integer vomitCount,
            final Integer diarrheaCount,
            final Boolean medicated,
            final Boolean coughing,
            final Boolean poorAppetite,
            final Boolean lowActivity,
            final String abnormalNote
    ) {
        return new DailyLog(
                pet,
                recordDate,
                weightKg,
                mealAmountG,
                waterAmountMl,
                walkDistanceM,
                walkDurationMinutes,
                sleepDurationMinutes,
                stoolCount,
                urineCount,
                vomitCount,
                diarrheaCount,
                medicated,
                coughing,
                poorAppetite,
                lowActivity,
                abnormalNote
        );
    }

    private DailyLog(
            final Pet pet,
            final LocalDate recordDate,
            final BigDecimal weightKg,
            final Integer mealAmountG,
            final Integer waterAmountMl,
            final Integer walkDistanceM,
            final Integer walkDurationMinutes,
            final Integer sleepDurationMinutes,
            final Integer stoolCount,
            final Integer urineCount,
            final Integer vomitCount,
            final Integer diarrheaCount,
            final Boolean medicated,
            final Boolean coughing,
            final Boolean poorAppetite,
            final Boolean lowActivity,
            final String abnormalNote
    ) {
        this.pet = pet;
        this.recordDate = recordDate;
        this.weightKg = weightKg;
        this.mealAmountG = mealAmountG;
        this.waterAmountMl = waterAmountMl;
        this.walkDistanceM = walkDistanceM;
        this.walkDurationMinutes = walkDurationMinutes;
        this.sleepDurationMinutes = sleepDurationMinutes;
        this.stoolCount = stoolCount;
        this.urineCount = urineCount;
        this.vomitCount = vomitCount;
        this.diarrheaCount = diarrheaCount;
        this.medicated = medicated;
        this.coughing = coughing;
        this.poorAppetite = poorAppetite;
        this.lowActivity = lowActivity;
        this.abnormalNote = abnormalNote;
    }

    public void update(
            final BigDecimal weightKg,
            final Integer mealAmountG,
            final Integer waterAmountMl,
            final Integer walkDistanceM,
            final Integer walkDurationMinutes,
            final Integer sleepDurationMinutes,
            final Integer stoolCount,
            final Integer urineCount,
            final Integer vomitCount,
            final Integer diarrheaCount,
            final Boolean medicated,
            final Boolean coughing,
            final Boolean poorAppetite,
            final Boolean lowActivity,
            final String abnormalNote
    ) {
        this.weightKg = weightKg;
        this.mealAmountG = mealAmountG;
        this.waterAmountMl = waterAmountMl;
        this.walkDistanceM = walkDistanceM;
        this.walkDurationMinutes = walkDurationMinutes;
        this.sleepDurationMinutes = sleepDurationMinutes;
        this.stoolCount = stoolCount;
        this.urineCount = urineCount;
        this.vomitCount = vomitCount;
        this.diarrheaCount = diarrheaCount;
        this.medicated = medicated;
        this.coughing = coughing;
        this.poorAppetite = poorAppetite;
        this.lowActivity = lowActivity;
        this.abnormalNote = abnormalNote;
    }
}
