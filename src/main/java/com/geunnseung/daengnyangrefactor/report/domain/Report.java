package com.geunnseung.daengnyangrefactor.report.domain;

import com.geunnseung.daengnyangrefactor.global.domain.BaseTimeEntity;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
        name = "reports",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_report_pet_id_type_period",
                        columnNames = {"pet_id", "type", "period_start", "period_end"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReportType type;

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "recorded_days", nullable = false)
    private Integer recordedDays;

    @Column(name = "average_weight_kg", precision = 5, scale = 2)
    private BigDecimal averageWeightKg;

    @Column(name = "average_meal_amount_g", precision = 10, scale = 2)
    private BigDecimal averageMealAmountG;

    @Column(name = "average_water_amount_ml", precision = 10, scale = 2)
    private BigDecimal averageWaterAmountMl;

    @Column(name = "total_walk_distance_m")
    private Integer totalWalkDistanceM;

    @Column(name = "total_walk_duration_minutes")
    private Integer totalWalkDurationMinutes;

    @Column(name = "average_sleep_duration_minutes", precision = 10, scale = 2)
    private BigDecimal averageSleepDurationMinutes;

    @Column(name = "total_stool_count")
    private Integer totalStoolCount;

    @Column(name = "total_urine_count")
    private Integer totalUrineCount;

    @Column(name = "total_vomit_count")
    private Integer totalVomitCount;

    @Column(name = "total_diarrhea_count")
    private Integer totalDiarrheaCount;

    @Column(name = "medicated_days")
    private Integer medicatedDays;

    @Column(name = "coughing_days")
    private Integer coughingDays;

    @Column(name = "poor_appetite_days")
    private Integer poorAppetiteDays;

    @Column(name = "low_activity_days")
    private Integer lowActivityDays;

    public static Report create(
            final Pet pet,
            final ReportType type,
            final LocalDate periodStart,
            final LocalDate periodEnd,
            final Integer recordedDays,
            final BigDecimal averageWeightKg,
            final BigDecimal averageMealAmountG,
            final BigDecimal averageWaterAmountMl,
            final Integer totalWalkDistanceM,
            final Integer totalWalkDurationMinutes,
            final BigDecimal averageSleepDurationMinutes,
            final Integer totalStoolCount,
            final Integer totalUrineCount,
            final Integer totalVomitCount,
            final Integer totalDiarrheaCount,
            final Integer medicatedDays,
            final Integer coughingDays,
            final Integer poorAppetiteDays,
            final Integer lowActivityDays
    ) {
        return new Report(
                pet,
                type,
                periodStart,
                periodEnd,
                recordedDays,
                averageWeightKg,
                averageMealAmountG,
                averageWaterAmountMl,
                totalWalkDistanceM,
                totalWalkDurationMinutes,
                averageSleepDurationMinutes,
                totalStoolCount,
                totalUrineCount,
                totalVomitCount,
                totalDiarrheaCount,
                medicatedDays,
                coughingDays,
                poorAppetiteDays,
                lowActivityDays
        );
    }

    private Report(
            final Pet pet,
            final ReportType type,
            final LocalDate periodStart,
            final LocalDate periodEnd,
            final Integer recordedDays,
            final BigDecimal averageWeightKg,
            final BigDecimal averageMealAmountG,
            final BigDecimal averageWaterAmountMl,
            final Integer totalWalkDistanceM,
            final Integer totalWalkDurationMinutes,
            final BigDecimal averageSleepDurationMinutes,
            final Integer totalStoolCount,
            final Integer totalUrineCount,
            final Integer totalVomitCount,
            final Integer totalDiarrheaCount,
            final Integer medicatedDays,
            final Integer coughingDays,
            final Integer poorAppetiteDays,
            final Integer lowActivityDays
    ) {
        this.pet = pet;
        this.type = type;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.recordedDays = recordedDays;
        this.averageWeightKg = averageWeightKg;
        this.averageMealAmountG = averageMealAmountG;
        this.averageWaterAmountMl = averageWaterAmountMl;
        this.totalWalkDistanceM = totalWalkDistanceM;
        this.totalWalkDurationMinutes = totalWalkDurationMinutes;
        this.averageSleepDurationMinutes = averageSleepDurationMinutes;
        this.totalStoolCount = totalStoolCount;
        this.totalUrineCount = totalUrineCount;
        this.totalVomitCount = totalVomitCount;
        this.totalDiarrheaCount = totalDiarrheaCount;
        this.medicatedDays = medicatedDays;
        this.coughingDays = coughingDays;
        this.poorAppetiteDays = poorAppetiteDays;
        this.lowActivityDays = lowActivityDays;
    }
}
