package com.geunnseung.daengnyangrefactor.report.repository;

import com.geunnseung.daengnyangrefactor.report.domain.Report;
import com.geunnseung.daengnyangrefactor.report.domain.ReportType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportRepository extends JpaRepository<Report, Long> {

    boolean existsByPetIdAndTypeAndPeriodStartAndPeriodEnd(
            Long petId,
            ReportType type,
            LocalDate periodStart,
            LocalDate periodEnd
    );

    Optional<Report> findByPetIdAndTypeAndPeriodStartAndPeriodEnd(
            Long petId,
            ReportType type,
            LocalDate periodStart,
            LocalDate periodEnd
    );

    @Query("""
            select report.pet.id
            from Report report
            where report.type = :type
              and report.periodStart = :periodStart
              and report.periodEnd = :periodEnd
            """)
    List<Long> findPetIdsByTypeAndPeriod(
            @Param("type") ReportType type,
            @Param("periodStart") LocalDate periodStart,
            @Param("periodEnd") LocalDate periodEnd
    );
}
