package com.geunnseung.daengnyangrefactor.dailylog.service;

import com.geunnseung.daengnyangrefactor.dailylog.api.dto.response.DailyLogResponse;
import com.geunnseung.daengnyangrefactor.dailylog.domain.DailyLog;
import com.geunnseung.daengnyangrefactor.dailylog.repository.DailyLogRepository;
import com.geunnseung.daengnyangrefactor.dailylog.service.command.DailyLogCreateCommand;
import com.geunnseung.daengnyangrefactor.dailylog.service.command.DailyLogUpdateCommand;
import com.geunnseung.daengnyangrefactor.global.exception.DaengnyangException;
import com.geunnseung.daengnyangrefactor.global.exception.ErrorCode;
import com.geunnseung.daengnyangrefactor.group.domain.Group;
import com.geunnseung.daengnyangrefactor.group.repository.UserGroupRepository;
import com.geunnseung.daengnyangrefactor.pet.domain.Pet;
import com.geunnseung.daengnyangrefactor.pet.repository.PetRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DailyLogService {

    private final PetRepository petRepository;
    private final UserGroupRepository userGroupRepository;
    private final DailyLogRepository dailyLogRepository;

    @Transactional
    public DailyLogResponse createDailyLog(
            final Long userId,
            final Long petId,
            final DailyLogCreateCommand command
    ) {
        Pet pet = findPet(petId);
        validatePetAccessible(userId, pet);
        validateHasAnyValue(command);
        validateNotExists(petId, command);

        DailyLog dailyLog = DailyLog.create(
                pet,
                command.recordDate(),
                command.weightKg(),
                command.mealAmountG(),
                command.waterAmountMl(),
                command.walkDistanceM(),
                command.walkDurationMinutes(),
                command.sleepDurationMinutes(),
                command.stoolCount(),
                command.urineCount(),
                command.vomitCount(),
                command.diarrheaCount(),
                command.medicated(),
                command.coughing(),
                command.poorAppetite(),
                command.lowActivity(),
                command.abnormalNote()
        );
        dailyLogRepository.save(dailyLog);

        return DailyLogResponse.from(dailyLog);
    }

    @Transactional(readOnly = true)
    public List<DailyLogResponse> getDailyLogsInPeriod(
            final Long userId,
            final Long petId,
            final LocalDate from,
            final LocalDate to
    ) {
        validateDateRange(from, to);

        Pet pet = findPet(petId);
        validatePetAccessible(userId, pet);

        List<DailyLog> dailyLogs = dailyLogRepository.findAllByPetIdAndRecordDateBetweenOrderByRecordDateAsc(
                petId,
                from,
                to
        );

        return dailyLogs.stream()
                .map(DailyLogResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DailyLogResponse getDailyLog(
            final Long userId,
            final Long petId,
            final LocalDate recordDate
    ) {
        Pet pet = findPet(petId);
        validatePetAccessible(userId, pet);

        DailyLog dailyLog = findDailyLog(petId, recordDate);

        return DailyLogResponse.from(dailyLog);
    }

    @Transactional
    public DailyLogResponse updateDailyLog(
            final Long userId,
            final Long petId,
            final LocalDate recordDate,
            final DailyLogUpdateCommand command
    ) {
        Pet pet = findPet(petId);
        validatePetAccessible(userId, pet);

        validateHasAnyValue(command);

        DailyLog dailyLog = findDailyLog(petId, recordDate);
        dailyLog.update(
                command.weightKg(),
                command.mealAmountG(),
                command.waterAmountMl(),
                command.walkDistanceM(),
                command.walkDurationMinutes(),
                command.sleepDurationMinutes(),
                command.stoolCount(),
                command.urineCount(),
                command.vomitCount(),
                command.diarrheaCount(),
                command.medicated(),
                command.coughing(),
                command.poorAppetite(),
                command.lowActivity(),
                command.abnormalNote()
        );

        return DailyLogResponse.from(dailyLog);
    }

    @Transactional
    public void deleteDailyLog(
            final Long userId,
            final Long petId,
            final LocalDate recordDate
    ) {
        Pet pet = findPet(petId);
        validatePetAccessible(userId, pet);

        DailyLog dailyLog = findDailyLog(petId, recordDate);
        dailyLogRepository.delete(dailyLog);
    }

    private Pet findPet(final Long petId) {
        return petRepository.findByIdWithGroup(petId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));
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

    private void validateHasAnyValue(final DailyLogCreateCommand command) {
        if (!command.hasAnyValue()) {
            throw new DaengnyangException(ErrorCode.INVALID_REQUEST);
        }
    }

    private void validateHasAnyValue(final DailyLogUpdateCommand command) {
        if (!command.hasAnyValue()) {
            throw new DaengnyangException(ErrorCode.INVALID_REQUEST);
        }
    }

    private void validateNotExists(final Long petId, final DailyLogCreateCommand command) {
        if (dailyLogRepository.existsByPetIdAndRecordDate(petId, command.recordDate())) {
            throw new DaengnyangException(ErrorCode.DAILY_LOG_ALREADY_EXISTS);
        }
    }

    private void validateDateRange(final LocalDate from, final LocalDate to) {
        if (from.isAfter(to)) {
            throw new DaengnyangException(ErrorCode.INVALID_REQUEST);
        }
    }

    private DailyLog findDailyLog(final Long petId, final LocalDate recordDate) {
        return dailyLogRepository.findByPetIdAndRecordDate(petId, recordDate)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.DAILY_LOG_NOT_FOUND));
    }
}
