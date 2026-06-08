package com.geunnseung.daengnyangrefactor.petpost;

import com.geunnseung.daengnyangrefactor.petpost.api.dto.response.PetPostDailyResponse;
import com.geunnseung.daengnyangrefactor.petpost.service.PetPostService;
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
public class DailyPetPostPerformanceTest {

    private static final Long USER_ID = 1L;
    private static final Long TARGET_PET_ID = 1L;
    private static final LocalDate TARGET_DATE = LocalDate.of(2026, 6, 8);

    private static final int PET_COUNT = 50;
    private static final int DAYS = 100;
    private static final int POSTS_PER_DAY = 10;

    @Autowired
    private PetPostService petPostService;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 날짜별_게시글_조회_성능_측정() {
        prepareData();

        Statistics statistics = statistics();

        long start = System.nanoTime();
        PetPostDailyResponse response = petPostService.getDailyPetPosts(USER_ID, TARGET_PET_ID, TARGET_DATE);
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        System.out.println("--------------------------------");
        System.out.println("전체 게시글 수: " + (PET_COUNT * DAYS * POSTS_PER_DAY));
        System.out.println("조회 게시글 수: " + response.posts().size());
        System.out.println("실행 시간: " + elapsedMs);
        System.out.println("쿼리 실행 수: " + statistics.getPrepareStatementCount());
        System.out.print("실행 계획:");
        System.out.println(explainQueryPlan());
    }

    private void prepareData() {
        insertUser();
        insertPets();
        insertPetPostsAndFiles();
    }

    private void insertUser() {
        LocalDateTime now = LocalDateTime.now();

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
                USER_ID,
                "test@test.com",
                "12345678",
                "test",
                "USER",
                now,
                now
        );
    }

    private void insertPets() {
        List<Object[]> petRows = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (long petId = 1; petId <= PET_COUNT; petId++) {
            petRows.add(new Object[]{
                    petId,
                    USER_ID,
                    null,
                    "dog" + petId,
                    "DOG",
                    "MALE",
                    null,
                    null,
                    null,
                    now,
                    now
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

    private void insertPetPostsAndFiles() {
        List<Object[]> postRows = new ArrayList<>();
        List<Object[]> fileRows = new ArrayList<>();

        LocalDate startDate = LocalDate.of(2026, 3, 1);
        LocalDateTime baseTime = LocalDateTime.of(2026, 3, 1, 0, 0);

        long postId = 1L;
        long fileId = 1L;

        for (long petId = 1; petId <= PET_COUNT; petId++) {
            for (int day = 0; day < DAYS; day++) {
                LocalDate recordDate = startDate.plusDays(day);

                for (int sequence = 0; sequence < POSTS_PER_DAY; sequence++) {
                    LocalDateTime createdAt = baseTime.plusSeconds(postId);

                    postRows.add(new Object[]{
                            postId,
                            petId,
                            USER_ID,
                            recordDate,
                            "test " + postId,
                            null,
                            createdAt,
                            createdAt
                    });

                    fileRows.add(new Object[]{
                            fileId++,
                            postId,
                            "IMAGE",
                            "https://example.com/" + postId + ".jpg",
                            "pet-posts/" + petId + "/" + postId,
                            "image/jpeg",
                            1024L,
                            createdAt,
                            createdAt
                    });

                    postId++;
                }
            }
        }

        jdbcTemplate.batchUpdate("""
                insert into pet_posts (
                    id,
                    pet_id,
                    user_id,
                    record_date,
                    content,
                    deleted_at,
                    created_at,
                    updated_at
                ) values (?, ?, ?, ?, ?, ?, ?, ?)
                """, postRows);

        jdbcTemplate.batchUpdate("""
                insert into pet_post_files (
                    id,
                    pet_post_id,
                    file_type,
                    file_url,
                    object_key,
                    content_type,
                    file_size,
                    created_at,
                    updated_at
                ) values (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """, fileRows);
    }

    private String explainQueryPlan() {
        return jdbcTemplate.queryForObject("""
                        explain
                        select file.*
                        from pet_post_files file
                        join pet_posts pet_post
                          on file.pet_post_id = pet_post.id
                        join users author
                          on pet_post.user_id = author.id
                        where pet_post.pet_id = ?
                          and pet_post.record_date = ?
                          and pet_post.deleted_at is null
                        order by pet_post.created_at asc, pet_post.id asc
                        """,
                String.class,
                TARGET_PET_ID,
                TARGET_DATE
        );
    }

    private Statistics statistics() {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        Statistics statistics = sessionFactory.getStatistics();
        statistics.setStatisticsEnabled(true);
        statistics.clear();
        return statistics;
    }
}
