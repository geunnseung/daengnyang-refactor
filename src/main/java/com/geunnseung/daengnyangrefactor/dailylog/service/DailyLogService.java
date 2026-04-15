package com.geunnseung.daengnyangrefactor.dailylog.service;

import com.geunnseung.daengnyangrefactor.dailylog.api.dto.request.DailyLogCreateRequest;
import com.geunnseung.daengnyangrefactor.dailylog.api.dto.request.DailyLogUpdateRequest;
import com.geunnseung.daengnyangrefactor.dailylog.api.dto.response.DailyLogResponse;
import com.geunnseung.daengnyangrefactor.dailylog.domain.DailyLog;
import com.geunnseung.daengnyangrefactor.dailylog.repository.DailyLogRepository;
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
            final DailyLogCreateRequest request
    ) {
        Pet pet = petRepository.findByIdWithGroup(petId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));
        validatePetAccessible(userId, pet);
        validateHasAnyValue(request);
        validateNotExists(petId, request);

        DailyLog dailyLog = DailyLog.create(
                pet,
                request.recordDate(),
                request.weightKg(),
                request.mealAmountG(),
                request.waterAmountMl(),
                request.walkDistanceM(),
                request.walkDurationMinutes(),
                request.sleepDurationMinutes(),
                request.stoolCount(),
                request.urineCount(),
                request.vomitCount(),
                request.diarrheaCount(),
                request.medicated(),
                request.coughing(),
                request.poorAppetite(),
                request.lowActivity(),
                request.abnormalNote()
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

        Pet pet = petRepository.findByIdWithGroup(petId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));
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
        Pet pet = petRepository.findByIdWithGroup(petId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));
        validatePetAccessible(userId, pet);

        DailyLog dailyLog = dailyLogRepository.findByPetIdAndRecordDate(petId, recordDate)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.DAILY_LOG_NOT_FOUND));

        return DailyLogResponse.from(dailyLog);
    }

    @Transactional
    public DailyLogResponse updateDailyLog(
            final Long userId,
            final Long petId,
            final LocalDate recordDate,
            final DailyLogUpdateRequest request
    ) {
        Pet pet = petRepository.findByIdWithGroup(petId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));
        validatePetAccessible(userId, pet);
        validateHasAnyValue(request);

        DailyLog dailyLog = dailyLogRepository.findByPetIdAndRecordDate(petId, recordDate)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.DAILY_LOG_NOT_FOUND));

        dailyLog.update(
                request.weightKg(),
                request.mealAmountG(),
                request.waterAmountMl(),
                request.walkDistanceM(),
                request.walkDurationMinutes(),
                request.sleepDurationMinutes(),
                request.stoolCount(),
                request.urineCount(),
                request.vomitCount(),
                request.diarrheaCount(),
                request.medicated(),
                request.coughing(),
                request.poorAppetite(),
                request.lowActivity(),
                request.abnormalNote()
        );

        return DailyLogResponse.from(dailyLog);
    }

    @Transactional
    public void deleteDailyLog(
            final Long userId,
            final Long petId,
            final LocalDate recordDate
    ) {
        Pet pet = petRepository.findByIdWithGroup(petId)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.PET_NOT_FOUND));
        validatePetAccessible(userId, pet);

        DailyLog dailyLog = dailyLogRepository.findByPetIdAndRecordDate(petId, recordDate)
                .orElseThrow(() -> new DaengnyangException(ErrorCode.DAILY_LOG_NOT_FOUND));

        dailyLogRepository.delete(dailyLog);
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

    private void validateHasAnyValue(final DailyLogCreateRequest request) {
        if (!request.hasAnyValue()) {
            throw new DaengnyangException(ErrorCode.INVALID_REQUEST);
        }
    }

    private void validateHasAnyValue(final DailyLogUpdateRequest request) {
        if (!request.hasAnyValue()) {
            throw new DaengnyangException(ErrorCode.INVALID_REQUEST);
        }
    }

    private void validateNotExists(final Long petId, final DailyLogCreateRequest request) {
        if (dailyLogRepository.existsByPetIdAndRecordDate(petId, request.recordDate())) {
            throw new DaengnyangException(ErrorCode.DAILY_LOG_ALREADY_EXISTS);
        }
    }

    private void validateDateRange(final LocalDate from, final LocalDate to) {
        if (from.isAfter(to)) {
            throw new DaengnyangException(ErrorCode.INVALID_REQUEST);
        }
    }
}
