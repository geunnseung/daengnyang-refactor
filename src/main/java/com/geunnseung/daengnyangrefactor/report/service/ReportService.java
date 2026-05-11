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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

    @Transactional
    public ReportResponse createReport(
            final Long petId,
            final ReportType type,
            final LocalDate periodStart,
            final LocalDate periodEnd
    ) {
        Pet pet = findPet(petId);
        validateNotExists(petId, type, periodStart, periodEnd);

        List<DailyLog> dailyLogs = dailyLogRepository.findAllByPetIdAndRecordDateBetweenOrderByRecordDateAsc(
                petId,
                periodStart,
                periodEnd
        );

        ReportStatistics statistics = calculateStatistics(dailyLogs);

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
        reportRepository.save(report);

        return ReportResponse.from(report);
    }

    @Transactional
    public void generateWeeklyReports(final LocalDate baseDate) {
        LocalDate periodStart = baseDate.minusWeeks(1)
                .with(DayOfWeek.MONDAY);
        LocalDate periodEnd = periodStart.plusDays(6);

        generateReports(ReportType.WEEKLY, periodStart, periodEnd);
    }

    @Transactional
    public void generateMonthlyReports(final LocalDate baseDate) {
        LocalDate previousMonth = baseDate.minusMonths(1);
        LocalDate periodStart = previousMonth.withDayOfMonth(1);
        LocalDate periodEnd = previousMonth.withDayOfMonth(previousMonth.lengthOfMonth());

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

        LocalDate periodStart = date.with(DayOfWeek.MONDAY);
        LocalDate periodEnd = periodStart.plusDays(6);

        Report report = findReport(
                petId,
                ReportType.WEEKLY,
                periodStart,
                periodEnd
        );

        return ReportResponse.from(report);
    }

    @Transactional(readOnly = true)
    public ReportResponse getMonthlyReport(
            final Long userId,
            final Long petId,
            final LocalDate date
    ) {
        Pet pet = findPetWithGroup(petId);
        validatePetAccessible(userId, pet);

        LocalDate periodStart = date.withDayOfMonth(1);
        LocalDate periodEnd = date.withDayOfMonth(date.lengthOfMonth());

        Report report = findReport(
                petId,
                ReportType.MONTHLY,
                periodStart,
                periodEnd
        );

        return ReportResponse.from(report);
    }

    private Pet findPet(final Long petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));
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

            createReport(pet.getId(), type, periodStart, periodEnd);
        }
    }

    private void validateNotExists(
            final Long petId,
            final ReportType type,
            final LocalDate periodStart,
            final LocalDate periodEnd
    ) {
        if (reportRepository.existsByPetIdAndTypeAndPeriodStartAndPeriodEnd(petId, type, periodStart, periodEnd)) {
            throw new DaengnyangException(ErrorCode.REPORT_ALREADY_EXISTS);
        }
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

    private ReportStatistics calculateStatistics(final List<DailyLog> dailyLogs) {
        return new ReportStatistics(
                dailyLogs.size(),
                average(dailyLogs.stream().map(DailyLog::getWeightKg).toList()),
                averageInteger(dailyLogs.stream().map(DailyLog::getMealAmountG).toList()),
                averageInteger(dailyLogs.stream().map(DailyLog::getWaterAmountMl).toList()),
                sumInteger(dailyLogs.stream().map(DailyLog::getWalkDistanceM).toList()),
                sumInteger(dailyLogs.stream().map(DailyLog::getWalkDurationMinutes).toList()),
                averageInteger(dailyLogs.stream().map(DailyLog::getSleepDurationMinutes).toList()),
                sumInteger(dailyLogs.stream().map(DailyLog::getStoolCount).toList()),
                sumInteger(dailyLogs.stream().map(DailyLog::getUrineCount).toList()),
                sumInteger(dailyLogs.stream().map(DailyLog::getVomitCount).toList()),
                sumInteger(dailyLogs.stream().map(DailyLog::getDiarrheaCount).toList()),
                countTrue(dailyLogs.stream().map(DailyLog::getMedicated).toList()),
                countTrue(dailyLogs.stream().map(DailyLog::getCoughing).toList()),
                countTrue(dailyLogs.stream().map(DailyLog::getPoorAppetite).toList()),
                countTrue(dailyLogs.stream().map(DailyLog::getLowActivity).toList())
        );
    }

    private BigDecimal average(final List<BigDecimal> values) {
        List<BigDecimal> recordedValues = values.stream()
                .filter(Objects::nonNull)
                .toList();

        if (recordedValues.isEmpty()) {
            return null;
        }

        BigDecimal sum = recordedValues.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.divide(BigDecimal.valueOf(recordedValues.size()), 2, RoundingMode.HALF_UP);
    }

    private BigDecimal averageInteger(final List<Integer> values) {
        List<Integer> recordedValues = values.stream()
                .filter(Objects::nonNull)
                .toList();

        if (recordedValues.isEmpty()) {
            return null;
        }

        int sum = recordedValues.stream()
                .mapToInt(Integer::intValue)
                .sum();

        return BigDecimal.valueOf(sum)
                .divide(BigDecimal.valueOf(recordedValues.size()), 2, RoundingMode.HALF_UP);
    }

    private Integer sumInteger(final List<Integer> values) {
        List<Integer> recordedValues = values.stream()
                .filter(Objects::nonNull)
                .toList();

        if (recordedValues.isEmpty()) {
            return null;
        }

        return recordedValues.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    private Integer countTrue(final List<Boolean> values) {
        List<Boolean> recordedValues = values.stream()
                .filter(Objects::nonNull)
                .toList();

        if (recordedValues.isEmpty()) {
            return null;
        }

        return (int) recordedValues.stream()
                .filter(Boolean::booleanValue)
                .count();
    }
}
