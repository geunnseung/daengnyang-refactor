package com.geunnseung.daengnyangrefactor.report;

import com.geunnseung.daengnyangrefactor.report.service.ReportService;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ReportPerformanceTest {

    private static final int PET_COUNT = 1000;
    private static final int DAILY_LOG_DAYS = 31;

    @Autowired
    private ReportService reportService;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    void 월간_리포트_생성_성능_측정() {
        prepareData();

        Statistics statistics = statistics();

        long start = System.nanoTime();
        reportService.generateMonthlyReports(LocalDate.of(2026, 6, 1));
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        System.out.println("--------------------------------");
        System.out.println("실행 시간: " + elapsedMs);
        System.out.println("쿼리 실행 수: " + statistics.getPrepareStatementCount());
    }

    private void prepareData() {
        insertUsers();
        insertPets();
        insertDailyLogs();
    }

    private void insertUsers() {
        jdbcTemplate.update("""
                        insert into users (
                            id,
                            email,
                            password,
                            nickname,
                            role,
                            created_at,
                            updated_at
                        ) values (?, ?, ?, ?, ?, ?, ?)
                        """,
                1L,
                "test@test.com",
                "12345678",
                "test",
                "USER",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private void insertPets() {
        List<Object[]> petRows = new ArrayList<>();

        for (long petId = 1; petId <= PET_COUNT; petId++) {
            petRows.add(new Object[]{
                    petId,
                    1L,
                    null,
                    "dog" + petId,
                    "DOG",
                    "MALE",
                    null,
                    null,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            });
        }

        jdbcTemplate.batchUpdate("""
                insert into pets (
                    id,
                    user_id,
                    group_id,
                    name,
                    species,
                    gender,
                    birth_date,
                    profile_image_url,
                    deleted_at,
                    created_at,
                    updated_at
                ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """, petRows);
    }

    private void insertDailyLogs() {
        List<Object[]> dailyLogRows = new ArrayList<>();
        LocalDate startDate = LocalDate.of(2026, 5, 1);
        LocalDateTime now = LocalDateTime.now();

        long dailyLogId = 1L;

        for (long petId = 1; petId <= PET_COUNT; petId++) {
            for (int day = 0; day < DAILY_LOG_DAYS; day++) {
                dailyLogRows.add(new Object[]{
                        dailyLogId++,
                        0L,
                        petId,
                        startDate.plusDays(day),
                        "7.77",
                        120,
                        300,
                        1500,
                        30,
                        600,
                        2,
                        4,
                        0,
                        0,
                        false,
                        false,
                        false,
                        false,
                        null,
                        now,
                        now
                });
            }
        }

        jdbcTemplate.batchUpdate("""
                insert into daily_logs (
                    id,
                    version,
                    pet_id,
                    record_date,
                    weight_kg,
                    meal_amount_g,
                    water_amount_ml,
                    walk_distance_m,
                    walk_duration_minutes,
                    sleep_duration_minutes,
                    stool_count,
                    urine_count,
                    vomit_count,
                    diarrhea_count,
                    medicated,
                    coughing,
                    poor_appetite,
                    low_activity,
                    abnormal_note,
                    created_at,
                    updated_at
                ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """, dailyLogRows);
    }

    private Statistics statistics() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();
        return statistics;
    }
}
