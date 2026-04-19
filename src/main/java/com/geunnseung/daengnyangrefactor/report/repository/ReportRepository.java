package com.geunnseung.daengnyangrefactor.report.repository;

import com.geunnseung.daengnyangrefactor.report.domain.Report;
import com.geunnseung.daengnyangrefactor.report.domain.ReportType;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByPetIdAndTypeAndPeriodStartAndPeriodEnd(
            Long petId,
            ReportType type,
            LocalDate periodStart,
            LocalDate periodEnd
    );
}
