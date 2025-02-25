package com.BaGulBaGul.BaGulBaGul.domain.ranking.repository;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.TagRankingItemInfo;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

@Repository
public class TagRankingRepositoryRedisImpl implements TagRankingRepository {

    //TAG_RANK_7DAYS
    private static final String KEY_7DAYS_FORMAT = "TAG_RANK_7DAYS";
    //TAG_RANK_DAY_{YYYY-MM-DD}
    private static final String KEY_DAY_FORMAT = "TAG_RANK_DAY_%tY-%<tm-%<td";
    private static final int SCAN_COUNT = 100;

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisSerializer keySerializer;
    private final RedisSerializer valueSerializer;

    public TagRankingRepositoryRedisImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.keySerializer = redisTemplate.getKeySerializer();
        this.valueSerializer = redisTemplate.getValueSerializer();
    }

    @RequiredArgsConstructor
    public static class TagRankingItemIterator implements Iterator<TagRankingItemInfo> {

        private final Cursor<Entry<String, String>> cursor;

        @Override
        public boolean hasNext() {
            return cursor.hasNext();
        }

        @Override
        public TagRankingItemInfo next() {
            Entry<String, String> next = cursor.next();
            return TagRankingItemInfo.builder()
                    .tag(next.getKey())
                    .referenceCount(Long.parseLong(next.getValue()))
                    .build();
        }
    }

    @Override
    public void increase7DaysTags(Collection<? extends TagRankingItemInfo> tags) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            String key = get7DaysKey();
            for(TagRankingItemInfo item : tags) {
                double incrementAmount = item.getReferenceCount();
                connection.zSetCommands().zIncrBy(
                        keySerializer.serialize(key),
                        incrementAmount,
                        valueSerializer.serialize(item.getTag().toString())
                );
            }
            return null;
        });
    }

    @Override
    public void decrease7DaysTags(Collection<? extends TagRankingItemInfo> tags) {
        increase7DaysTags(
                tags.stream()
                        .map(tag -> new TagRankingItemInfo(tag.getTag(), -tag.getReferenceCount()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void deleteAll7DaysTags() {
        redisTemplate.delete(get7DaysKey());
    }

    @Override
    public List<String> getTopKTagsFrom7Days(int k) {
        return redisTemplate.opsForZSet().reverseRange(
                get7DaysKey(),
                0,
                k
        ).stream().collect(Collectors.toList());
    }

    @Override
    public void increaseDayTags(LocalDateTime time, Collection<? extends TagRankingItemInfo> tags) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for(TagRankingItemInfo tag : tags) {
                connection.hashCommands().hIncrBy(
                        keySerializer.serialize(getDayKey(time)),
                        valueSerializer.serialize(tag.getTag()),
                        tag.getReferenceCount()
                );
            }
            return null;
        });
    }

    @Override
    public void decreaseDayTags(LocalDateTime time, Collection<? extends TagRankingItemInfo> tags) {
        increaseDayTags(
                time,
                tags.stream().map(tag -> TagRankingItemInfo.builder()
                                .tag(tag.getTag())
                                .referenceCount(-tag.getReferenceCount())
                                .build())
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Iterator<TagRankingItemInfo> getDayTagIterator(LocalDateTime time) {
        Cursor<Entry<String, String>> cursor = redisTemplate.<String, String>opsForHash().scan(
                getDayKey(time),
                ScanOptions.scanOptions().count(SCAN_COUNT).build()
        );
        return new TagRankingItemIterator(cursor);
    }

    @Override
    public void deleteAllDayTagsByTime(LocalDateTime time) {
        redisTemplate.delete(getDayKey(time));
    }

    private String get7DaysKey() {
        return KEY_7DAYS_FORMAT;
    }

    private String getDayKey(LocalDateTime time) {
        return String.format(KEY_DAY_FORMAT, time);
    }
}
