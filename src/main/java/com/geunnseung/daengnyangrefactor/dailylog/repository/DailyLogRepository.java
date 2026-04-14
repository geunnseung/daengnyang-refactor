package com.geunnseung.daengnyangrefactor.dailylog.repository;

import com.geunnseung.daengnyangrefactor.dailylog.domain.DailyLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

    boolean existsByPetIdAndRecordDate(Long petId, LocalDate recordDate);

    List<DailyLog> findAllByPetIdAndRecordDateBetweenOrderByRecordDateAsc(
            Long petId,
            LocalDate from,
            LocalDate to
    );

    Optional<DailyLog> findByPetIdAndRecordDate(Long petId, LocalDate recordDate);
}
