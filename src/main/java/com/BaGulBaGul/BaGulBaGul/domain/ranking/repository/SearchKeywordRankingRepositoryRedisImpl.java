package com.BaGulBaGul.BaGulBaGul.domain.ranking.repository;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.SearchKeywordRankingItemInfo;
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
public class SearchKeywordRankingRepositoryRedisImpl implements SearchKeywordRankingRepository {

    //TAG_RANK_7DAYS
    private static final String KEY_7DAYS_FORMAT = "KEYWORD_RANK_7DAYS";
    //TAG_RANK_DAY_{YYYY-MM-DD}
    private static final String KEY_DAY_FORMAT = "KEYWORD_RANK_DAY_%tY-%<tm-%<td";
    private static final int SCAN_COUNT = 100;

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisSerializer keySerializer;
    private final RedisSerializer valueSerializer;

    public SearchKeywordRankingRepositoryRedisImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.keySerializer = redisTemplate.getKeySerializer();
        this.valueSerializer = redisTemplate.getValueSerializer();
    }

    @RequiredArgsConstructor
    public static class KeywordRankingItemIterator implements Iterator<SearchKeywordRankingItemInfo> {

        private final Cursor<Entry<String, String>> cursor;

        @Override
        public boolean hasNext() {
            return cursor.hasNext();
        }

        @Override
        public SearchKeywordRankingItemInfo next() {
            Entry<String, String> next = cursor.next();
            return SearchKeywordRankingItemInfo.builder()
                    .keyword(next.getKey())
                    .referenceCount(Long.parseLong(next.getValue()))
                    .build();
        }
    }

    @Override
    public void increase7DaysKeywords(Collection<? extends SearchKeywordRankingItemInfo> keywords) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            String key = get7DaysKey();
            for(SearchKeywordRankingItemInfo item : keywords) {
                double incrementAmount = item.getReferenceCount();
                connection.zSetCommands().zIncrBy(
                        keySerializer.serialize(key),
                        incrementAmount,
                        valueSerializer.serialize(item.getKeyword().toString())
                );
            }
            return null;
        });
    }

    @Override
    public void decrease7DaysKeywords(Collection<? extends SearchKeywordRankingItemInfo> keywords) {
        increase7DaysKeywords(
                keywords.stream()
                        .map(keyword -> new SearchKeywordRankingItemInfo(
                                keyword.getKeyword(), -keyword.getReferenceCount())
                        ).collect(Collectors.toList())
        );
    }

    @Override
    public void deleteAll7DaysKeywords() {
        redisTemplate.delete(get7DaysKey());
    }

    @Override
    public List<String> getTopKKeywordsFrom7Days(int k) {
        return redisTemplate.opsForZSet().reverseRange(
                get7DaysKey(),
                0,
                k
        ).stream().collect(Collectors.toList());
    }

    @Override
    public void increaseDayKeywords(LocalDateTime time, Collection<? extends SearchKeywordRankingItemInfo> keywords) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for(SearchKeywordRankingItemInfo keyword : keywords) {
                connection.hashCommands().hIncrBy(
                        keySerializer.serialize(getDayKey(time)),
                        valueSerializer.serialize(keyword.getKeyword()),
                        keyword.getReferenceCount()
                );
            }
            return null;
        });
    }

    @Override
    public void decreaseDayKeywords(LocalDateTime time, Collection<? extends SearchKeywordRankingItemInfo> keywords) {
        increaseDayKeywords(
                time,
                keywords.stream().map(keyword -> SearchKeywordRankingItemInfo.builder()
                                .keyword(keyword.getKeyword())
                                .referenceCount(-keyword.getReferenceCount())
                                .build())
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Iterator<SearchKeywordRankingItemInfo> getDayKeywordsIterator(LocalDateTime time) {
        Cursor<Entry<String, String>> cursor = redisTemplate.<String, String>opsForHash().scan(
                getDayKey(time),
                ScanOptions.scanOptions().count(SCAN_COUNT).build()
        );
        return new KeywordRankingItemIterator(cursor);
    }

    @Override
    public void deleteAllDayKeywordsByTime(LocalDateTime time) {
        redisTemplate.delete(getDayKey(time));
    }

    private String get7DaysKey() {
        return KEY_7DAYS_FORMAT;
    }

    private String getDayKey(LocalDateTime time) {
        time = time.withHour(0).withMinute(0).withSecond(0).withNano(0);
        return String.format(KEY_DAY_FORMAT, time);
    }
}
