package com.geunnseung.daengnyangrefactor.petpost.service.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geunnseung.daengnyangrefactor.petpost.api.dto.response.PetPostDailyResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PetPostDailyCache {

    private static final String KEY_PREFIX = "pet-post:daily:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public Optional<PetPostDailyResponse> find(
            final Long petId,
            final LocalDate recordDate
    ) {
        String key = generateKey(petId, recordDate);

        try {
            String cachedValue = redisTemplate.opsForValue().get(key);
            if (cachedValue == null) {
                return Optional.empty();
            }

            return Optional.of(objectMapper.readValue(cachedValue, PetPostDailyResponse.class));
        } catch (RuntimeException | JsonProcessingException exception) {
            log.warn("일별 게시글 캐시 조회 실패, key={}", key, exception);
            return Optional.empty();
        }
    }

    public void put(
            final Long petId,
            final LocalDate recordDate,
            final PetPostDailyResponse response
    ) {
        String key = generateKey(petId, recordDate);

        try {
            String cacheValue = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(key, cacheValue, CACHE_TTL);
        } catch (RuntimeException | JsonProcessingException exception) {
            log.warn("일별 게시글 캐시 저장 실패, key={}", key, exception);
        }
    }

    public void evict(
            final Long petId,
            final LocalDate recordDate
    ) {
        String key = generateKey(petId, recordDate);

        try {
            redisTemplate.delete(key);
        } catch (RuntimeException exception) {
            log.warn("일별 게시글 캐시 삭제 실패, key={}", key, exception);
        }
    }

    private String generateKey(
            final Long petId,
            final LocalDate recordDate
    ) {
        return KEY_PREFIX + petId + ":" + recordDate;
    }
}
