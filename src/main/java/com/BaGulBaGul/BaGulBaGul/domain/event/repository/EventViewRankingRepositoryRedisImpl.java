package com.BaGulBaGul.BaGulBaGul.domain.event.repository;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EventViewRankingRepositoryRedisImpl implements EventViewRankingRepository {

    //EVENT_VIEW_RANK_7DAYS_{EVENT_TYPE}
    private static final String KEY_7DAYS_FORMAT = "EVENT_VIEW_RANK_7DAYS_%s";
    //EVENT_VIEW_RANK_DAY_{EVENT_TYPE}_{YYYY-MM-DD}
    private static final String KEY_DAY_FORMAT = "EVENT_VIEW_RANK_DAY_%s_%tY-%<tm-%<td";
    private static final int SCAN_COUNT = 100;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void increase7DaysViewCount(Long eventId, EventType eventType, Long amount) {
        redisTemplate.opsForZSet().incrementScore(
                get7DaysKey(eventType),
                eventId.toString(),
                amount
        );

    }

    @Override
    public void decrease7DaysViewCount(Long eventId, EventType eventType, Long amount) {
        increase7DaysViewCount(eventId, eventType, -amount);
    }

    @Override
    public List<Long> getTopKRankEventFrom7DaysViewCount(EventType eventType, int k) {
        Set<String> topKSet = redisTemplate.opsForZSet().reverseRange(
                get7DaysKey(eventType),
                0,
                k
        );
        return topKSet.stream().map(Long::parseLong).collect(Collectors.toList());
    }

    @Override
    public void increaseDayViewCount(Long eventId, EventType eventType, LocalDateTime time, Long amount) {
        redisTemplate.opsForHash().increment(
                getDayKey(eventType, time),
                String.valueOf(eventId),
                amount
        );
    }

    @Override
    public Iterator<Entry<String, String>> getDayViewCountIterator(EventType eventType, LocalDateTime time) {
        ScanOptions scanOptions = ScanOptions.scanOptions().count(SCAN_COUNT).build();
        return redisTemplate.<String, String>opsForHash().scan(
                getDayKey(eventType, time),
                scanOptions
        );
    }

    @Override
    public void deleteAllDayViewCountByTime(EventType eventType, LocalDateTime time) {
        redisTemplate.delete(getDayKey(eventType, time));
    }

    private String get7DaysKey(EventType eventType) {
        return String.format(KEY_7DAYS_FORMAT, eventType);
    }

    private String getDayKey(EventType eventType, LocalDateTime localDateTime) {
        return String.format(KEY_DAY_FORMAT, eventType, localDateTime);
    }
}