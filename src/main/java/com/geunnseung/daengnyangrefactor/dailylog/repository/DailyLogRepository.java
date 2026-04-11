package com.geunnseung.daengnyangrefactor.dailylog.repository;

import com.geunnseung.daengnyangrefactor.dailylog.domain.DailyLog;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

    boolean existsByPetIdAndRecordDate(Long petId, LocalDate recordDate);
}
