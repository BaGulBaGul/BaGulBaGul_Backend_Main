package com.BaGulBaGul.BaGulBaGul.domain.ranking.repository;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.EventViewsRankingItemInfo;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

@Repository
public class EventViewRankingRepositoryRedisImpl implements EventViewRankingRepository {

    //EVENT_VIEW_RANK_7DAYS_{EVENT_TYPE}
    private static final String KEY_7DAYS_FORMAT = "EVENT_VIEW_RANK_7DAYS_%s";
    //EVENT_VIEW_RANK_DAY_{EVENT_TYPE}_{YYYY-MM-DD}
    private static final String KEY_DAY_FORMAT = "EVENT_VIEW_RANK_DAY_%s_%tY-%<tm-%<td";
    private static final int SCAN_COUNT = 100;

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisSerializer keySerializer;
    private final RedisSerializer valueSerializer;

    public EventViewRankingRepositoryRedisImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.keySerializer = redisTemplate.getKeySerializer();
        this.valueSerializer = redisTemplate.getValueSerializer();
    }

    @RequiredArgsConstructor
    static class ViewsRankingItemIterator implements Iterator<EventViewsRankingItemInfo> {

        private final Cursor<Entry<String, String>> cursor;

        @Override
        public boolean hasNext() {
            return cursor.hasNext();
        }

        @Override
        public EventViewsRankingItemInfo next() {
            Entry<String, String> entry = cursor.next();
            return new EventViewsRankingItemInfo(
                    Long.parseLong(entry.getKey()),
                    Long.parseLong(entry.getValue())
            );
        }
    }

    @Override
    public void increase7DaysViewCount(Long eventId, EventType eventType, Long amount) {
        redisTemplate.opsForZSet().incrementScore(
                get7DaysKey(eventType),
                eventId.toString(),
                amount
        );

    }

    public void increase7DaysViewCounts(EventType eventType, Collection<? extends EventViewsRankingItemInfo> viewCounts) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            String key = get7DaysKey(eventType);
            for(EventViewsRankingItemInfo item : viewCounts) {
                double incrementAmount = item.getViewCount();
                connection.zSetCommands().zIncrBy(
                        keySerializer.serialize(key),
                        incrementAmount,
                        valueSerializer.serialize(item.getEventId().toString())
                );
            }
            return null;
        });
    }

    @Override
    public void decrease7DaysViewCount(Long eventId, EventType eventType, Long amount) {
        increase7DaysViewCount(eventId, eventType, -amount);
    }

    @Override
    public void decrease7DaysViewCounts(
            EventType eventType,
            Collection<? extends EventViewsRankingItemInfo> viewCounts
    ) {
        increase7DaysViewCounts(
                eventType,
                viewCounts.stream()
                        .map(item -> new EventViewsRankingItemInfo(item.getEventId(), -item.getViewCount()))
                        .collect(Collectors.toList())
        );
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
    public void deleteAll7DaysViewCount(EventType eventType) {
        redisTemplate.delete(get7DaysKey(eventType));
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
    public void increaseDayViewCounts(
            EventType eventType,
            LocalDateTime time,
            Collection<? extends EventViewsRankingItemInfo> viewCounts
    ) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            String key = getDayKey(eventType, time);
            for(EventViewsRankingItemInfo item : viewCounts) {
                connection.hashCommands().hIncrBy(
                        keySerializer.serialize(key),
                        valueSerializer.serialize(item.getEventId().toString()),
                        item.getViewCount()
                );
            }
            return null;
        });
    }

    @Override
    public Iterator<EventViewsRankingItemInfo> getDayViewCountIterator(EventType eventType, LocalDateTime time) {
        ScanOptions scanOptions = ScanOptions.scanOptions().count(SCAN_COUNT).build();
        Cursor<Entry<String, String>> cursor = redisTemplate.<String, String>opsForHash().scan(
                getDayKey(eventType, time),
                scanOptions
        );
        return new ViewsRankingItemIterator(cursor);
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