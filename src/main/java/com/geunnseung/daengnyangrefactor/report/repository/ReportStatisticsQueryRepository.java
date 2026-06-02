package com.geunnseung.daengnyangrefactor.report.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportStatisticsQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<ReportStatisticsResult> findAllByRecordDateBetween(
            final LocalDate periodStart,
            final LocalDate periodEnd
    ) {
        return jdbcTemplate.query("""
                        select
                            p.id as pet_id,
                            count(dl.id) as recorded_days,
                            round(avg(dl.weight_kg), 2) as average_weight_kg,
                            round(avg(dl.meal_amount_g), 2) as average_meal_amount_g,
                            round(avg(dl.water_amount_ml), 2) as average_water_amount_ml,
                            sum(dl.walk_distance_m) as total_walk_distance_m,
                            sum(dl.walk_duration_minutes) as total_walk_duration_minutes,
                            round(avg(dl.sleep_duration_minutes), 2) as average_sleep_duration_minutes,
                            sum(dl.stool_count) as total_stool_count,
                            sum(dl.urine_count) as total_urine_count,
                            sum(dl.vomit_count) as total_vomit_count,
                            sum(dl.diarrhea_count) as total_diarrhea_count,
                            case when count(dl.medicated) = 0 then null
                                 else sum(case when dl.medicated is true then 1 else 0 end)
                            end as medicated_days,
                            case when count(dl.coughing) = 0 then null
                                 else sum(case when dl.coughing is true then 1 else 0 end)
                            end as coughing_days,
                            case when count(dl.poor_appetite) = 0 then null
                                 else sum(case when dl.poor_appetite is true then 1 else 0 end)
                            end as poor_appetite_days,
                            case when count(dl.low_activity) = 0 then null
                                 else sum(case when dl.low_activity is true then 1 else 0 end)
                            end as low_activity_days
                        from pets p
                        left join daily_logs dl
                          on dl.pet_id = p.id
                         and dl.record_date between ? and ?
                        where p.deleted_at is null
                        group by p.id
                        """,
                this::mapToResult,
                periodStart,
                periodEnd
        );
    }

    private ReportStatisticsResult mapToResult(final ResultSet rs, final int rowNum) throws SQLException {
        return new ReportStatisticsResult(
                rs.getLong("pet_id"),
                getInteger(rs, "recorded_days"),
                rs.getBigDecimal("average_weight_kg"),
                rs.getBigDecimal("average_meal_amount_g"),
                rs.getBigDecimal("average_water_amount_ml"),
                getInteger(rs, "total_walk_distance_m"),
                getInteger(rs, "total_walk_duration_minutes"),
                rs.getBigDecimal("average_sleep_duration_minutes"),
                getInteger(rs, "total_stool_count"),
                getInteger(rs, "total_urine_count"),
                getInteger(rs, "total_vomit_count"),
                getInteger(rs, "total_diarrhea_count"),
                getInteger(rs, "medicated_days"),
                getInteger(rs, "coughing_days"),
                getInteger(rs, "poor_appetite_days"),
                getInteger(rs, "low_activity_days")
        );
    }

    private Integer getInteger(final ResultSet rs, final String columnLabel) throws SQLException {
        int value = rs.getInt(columnLabel);
        return rs.wasNull() ? null : value;
    }
}
