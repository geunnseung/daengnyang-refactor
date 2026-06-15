package com.geunnseung.daengnyangrefactor.petpost.service.cache;

import com.geunnseung.daengnyangrefactor.petpost.api.dto.response.PetPostDailyResponse;
import com.geunnseung.daengnyangrefactor.petpost.api.dto.response.PetPostDetailResponse;
import com.geunnseung.daengnyangrefactor.petpost.domain.PetPostFile;
import com.geunnseung.daengnyangrefactor.petpost.repository.PetPostFileRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PetPostDailyCacheEventHandler {

    private final PetPostDailyCache petPostDailyCache;
    private final PetPostFileRepository petPostFileRepository;

    @Async("cacheTaskExecutor")
    @Transactional(readOnly = true)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void refresh(final PetPostDailyCacheRefreshEvent event) {
        PetPostDailyResponse response = loadPosts(event.petId(), event.recordDate());
        petPostDailyCache.put(event.petId(), event.recordDate(), response);
    }

    @Async("cacheTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void evict(final PetPostDailyCacheEvictEvent event) {
        petPostDailyCache.evict(event.petId(), event.recordDate());
    }

    private PetPostDailyResponse loadPosts(
            final Long petId,
            final LocalDate recordDate
    ) {
        List<PetPostFile> files = petPostFileRepository.findAllWithPetPostAndAuthorByPetIdAndRecordDate(
                petId,
                recordDate
        );

        List<PetPostDetailResponse> posts = files.stream()
                .map(file -> PetPostDetailResponse.of(file.getPetPost(), file))
                .toList();

        return PetPostDailyResponse.of(recordDate, posts);
    }
}
