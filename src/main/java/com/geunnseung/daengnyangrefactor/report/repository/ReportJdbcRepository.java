package com.geunnseung.daengnyangrefactor.report.repository;

import com.geunnseung.daengnyangrefactor.report.domain.ReportType;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportJdbcRepository {

    private static final int BATCH_SIZE = 1000;

    private final JdbcTemplate jdbcTemplate;

    public void batchInsert(
            final List<ReportStatisticsResult> results,
            final ReportType type,
            final LocalDate periodStart,
            final LocalDate periodEnd
    ) {
        if (results.isEmpty()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.batchUpdate("""
                        insert into reports (
                            pet_id,
                            type,
                            period_start,
                            period_end,
                            recorded_days,
                            average_weight_kg,
                            average_meal_amount_g,
                            average_water_amount_ml,
                            total_walk_distance_m,
                            total_walk_duration_minutes,
                            average_sleep_duration_minutes,
                            total_stool_count,
                            total_urine_count,
                            total_vomit_count,
                            total_diarrhea_count,
                            medicated_days,
                            coughing_days,
                            poor_appetite_days,
                            low_activity_days,
                            created_at,
                            updated_at
                        ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                        """,
                results,
                BATCH_SIZE,
                (ps, result) -> setValues(ps, result, type, periodStart, periodEnd, now)
        );
    }

    private void setValues(
            final PreparedStatement ps,
            final ReportStatisticsResult result,
            final ReportType type,
            final LocalDate periodStart,
            final LocalDate periodEnd,
            final LocalDateTime now
    ) throws SQLException {
        ps.setLong(1, result.petId());
        ps.setString(2, type.name());
        ps.setObject(3, periodStart);
        ps.setObject(4, periodEnd);
        ps.setInt(5, result.recordedDays());
        setBigDecimal(ps, 6, result.averageWeightKg());
        setBigDecimal(ps, 7, result.averageMealAmountG());
        setBigDecimal(ps, 8, result.averageWaterAmountMl());
        setInteger(ps, 9, result.totalWalkDistanceM());
        setInteger(ps, 10, result.totalWalkDurationMinutes());
        setBigDecimal(ps, 11, result.averageSleepDurationMinutes());
        setInteger(ps, 12, result.totalStoolCount());
        setInteger(ps, 13, result.totalUrineCount());
        setInteger(ps, 14, result.totalVomitCount());
        setInteger(ps, 15, result.totalDiarrheaCount());
        setInteger(ps, 16, result.medicatedDays());
        setInteger(ps, 17, result.coughingDays());
        setInteger(ps, 18, result.poorAppetiteDays());
        setInteger(ps, 19, result.lowActivityDays());
        ps.setObject(20, now);
        ps.setObject(21, now);
    }

    private void setInteger(
            final PreparedStatement ps,
            final int index,
            final Integer value
    ) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
            return;
        }

        ps.setInt(index, value);
    }

    private void setBigDecimal(
            final PreparedStatement ps,
            final int index,
            final BigDecimal value
    ) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.DECIMAL);
            return;
        }

        ps.setBigDecimal(index, value);
    }
}
