package com.geunnseung.daengnyangrefactor.report.service;

import com.geunnseung.daengnyangrefactor.dailylog.domain.DailyLog;
import com.geunnseung.daengnyangrefactor.dailylog.repository.DailyLogRepository;
import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.group.repository.UserGroupRepository;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import com.geunnseung.daengnyangrefactor.pet.repository.PetRepository;
import com.geunnseung.daengnyangrefactor.report.api.dto.response.ReportResponse;
import com.geunnseung.daengnyangrefactor.report.domain.Report;
import com.geunnseung.daengnyangrefactor.report.domain.ReportType;
import com.geunnseung.daengnyangrefactor.report.repository.ReportRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {

    private final PetRepository petRepository;
    private final DailyLogRepository dailyLogRepository;
    private final ReportRepository reportRepository;
    private final UserGroupRepository userGroupRepository;
    private final ReportStatisticsCalculator reportStatisticsCalculator;

    @Transactional
    public void generateWeeklyReports(final LocalDate baseDate) {
        LocalDate periodStart = getWeeklyPeriodStart(baseDate.minusWeeks(1));
        LocalDate periodEnd = periodStart.plusDays(6);

        generateReports(ReportType.WEEKLY, periodStart, periodEnd);
    }

    @Transactional
    public void generateMonthlyReports(final LocalDate baseDate) {
        LocalDate periodStart = getMonthlyPeriodStart(baseDate.minusMonths(1));
        LocalDate periodEnd = periodStart.withDayOfMonth(periodStart.lengthOfMonth());

        generateReports(ReportType.MONTHLY, periodStart, periodEnd);
    }

    @Transactional(readOnly = true)
    public ReportResponse getWeeklyReport(
            final Long userId,
            final Long petId,
            final LocalDate date
    ) {
        Pet pet = findPetWithGroup(petId);
        validatePetAccessible(userId, pet);

        LocalDate periodStart = getWeeklyPeriodStart(date);
        LocalDate periodEnd = periodStart.plusDays(6);

        Report report = findReport(
                petId,
                ReportType.WEEKLY,
                periodStart,
                periodEnd
        );

        return ReportResponse.of(report, petId);
    }

    @Transactional(readOnly = true)
    public ReportResponse getMonthlyReport(
            final Long userId,
            final Long petId,
            final LocalDate date
    ) {
        Pet pet = findPetWithGroup(petId);
        validatePetAccessible(userId, pet);

        LocalDate periodStart = getMonthlyPeriodStart(date);
        LocalDate periodEnd = periodStart.withDayOfMonth(periodStart.lengthOfMonth());

        Report report = findReport(
                petId,
                ReportType.MONTHLY,
                periodStart,
                periodEnd
        );

        return ReportResponse.of(report, petId);
    }

    private Pet findPetWithGroup(final Long petId) {
        return petRepository.findByIdWithGroup(petId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));
    }

    private Report findReport(
            final Long petId,
            final ReportType type,
            final LocalDate periodStart,
            final LocalDate periodEnd
    ) {
        return reportRepository.findByPetIdAndTypeAndPeriodStartAndPeriodEnd(
                        petId,
                        type,
                        periodStart,
                        periodEnd
                )
                .orElseThrow(() -> new DaengnyangException(ErrorCode.REPORT_NOT_FOUND));
    }

    private void validatePetAccessible(final Long userId, final Pet pet) {
        if (pet.getOwner().getId().equals(userId)) {
            return;
        }

        Group group = pet.getGroup();
        if (group != null && userGroupRepository.existsByUserIdAndGroupId(userId, group.getId())) {
            return;
        }

        throw new DaengnyangException(ErrorCode.PET_NOT_FOUND);
    }

    private LocalDate getWeeklyPeriodStart(final LocalDate date) {
        return date.with(DayOfWeek.MONDAY);
    }

    private LocalDate getMonthlyPeriodStart(final LocalDate date) {
        return date.withDayOfMonth(1);
    }

    private void generateReports(
            final ReportType type,
            final LocalDate periodStart,
            final LocalDate periodEnd
    ) {
        List<Pet> pets = petRepository.findAll();

        for (Pet pet : pets) {
            if (reportRepository.existsByPetIdAndTypeAndPeriodStartAndPeriodEnd(
                    pet.getId(),
                    type,
                    periodStart,
                    periodEnd
            )) {
                continue;
            }

            saveReport(
                    pet,
                    type,
                    periodStart,
                    periodEnd
            );
        }
    }

    private Report saveReport(
            final Pet pet,
            final ReportType type,
            final LocalDate periodStart,
            final LocalDate periodEnd
    ) {
        List<DailyLog> dailyLogs = dailyLogRepository.findAllByPetIdAndRecordDateBetweenOrderByRecordDateAsc(
                pet.getId(),
                periodStart,
                periodEnd
        );

        ReportStatistics statistics = reportStatisticsCalculator.calculate(dailyLogs);

        Report report = Report.create(
                pet,
                type,
                periodStart,
                periodEnd,
                statistics.recordedDays(),
                statistics.averageWeightKg(),
                statistics.averageMealAmountG(),
                statistics.averageWaterAmountMl(),
                statistics.totalWalkDistanceM(),
                statistics.totalWalkDurationMinutes(),
                statistics.averageSleepDurationMinutes(),
                statistics.totalStoolCount(),
                statistics.totalUrineCount(),
                statistics.totalVomitCount(),
                statistics.totalDiarrheaCount(),
                statistics.medicatedDays(),
                statistics.coughingDays(),
                statistics.poorAppetiteDays(),
                statistics.lowActivityDays()
        );
        return reportRepository.save(report);
    }
}
