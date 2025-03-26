package com.BaGulBaGul.BaGulBaGul.domain.ranking.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.TagRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ExtendWith(AllTestContainerExtension.class)
@ActiveProfiles("test")
class TagRankingRepositoryRedisImplTest {
    @Autowired
    TagRankingRepositoryRedisImpl tagRankingRepository;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Nested
    @DisplayName("최근 7일 태그 조회수 랭킹")
    class SevenDays {
        String sevenDaysKey;
        @BeforeEach
        void init() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method privateMethod = TagRankingRepositoryRedisImpl.class
                    .getDeclaredMethod("get7DaysKey");
            privateMethod.setAccessible(true);
            sevenDaysKey = (String)privateMethod.invoke(tagRankingRepository);
        }

        @AfterEach
        void tearDown() {
            redisTemplate.delete(sevenDaysKey);
        }

        @Test
        @DisplayName("태그 조회수 배치 증가 테스트")
        void increaseViewCountsTest() {
            //given
            List<TagRankingItemInfo> items = new ArrayList<>();
            int count = 100;
            for(int i = 0; i < count; i++) {
                items.add(new TagRankingItemInfo(String.valueOf(i), (long) i));
            }
            //when
            tagRankingRepository.increase7DaysTags(items);
            tagRankingRepository.increase7DaysTags(items);

            //then
            List<TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().rangeWithScores(
                    sevenDaysKey,
                    0,
                    count
            ).stream().collect(Collectors.toList());

            for(int i=0; i<count; i++) {
                assertThat(typedTuples.get(i).getScore()).isEqualTo(i * 2);
            }
        }

        @Test
        @DisplayName("태그 조회수 배치 감소 테스트")
        void decreaseViewCountsTest() {
            //given
            List<TagRankingItemInfo> items = new ArrayList<>();
            int count = 100;
            for(int i = 0; i < count; i++) {
                items.add(new TagRankingItemInfo(String.valueOf(i), (long) i));
            }
            //when
            tagRankingRepository.decrease7DaysTags(items);
            tagRankingRepository.decrease7DaysTags(items);

            //then
            List<TypedTuple<String>> typedTuples = redisTemplate.opsForZSet().reverseRangeWithScores(
                    sevenDaysKey,
                    0,
                    count
            ).stream().collect(Collectors.toList());

            for(int i=0; i<count; i++) {
                assertThat(typedTuples.get(i).getScore()).isEqualTo(-i * 2);
            }
        }

        @Test
        @DisplayName("태그 조회수 내림차순 조회가 작동하는지 랜덤값으로 테스트")
        void getTopKRankEventTest() {
            //given
            Random random = new Random();
            Map<Long, String> countMap = new TreeMap<>(Comparator.reverseOrder());
            int count = 100;
            Long maxView = 1000000000L;
            for(int i=0; i<count; i++) {
                countMap.put(random.nextLong() % maxView, String.valueOf(i));
            }
            List<TagRankingItemInfo> items = countMap.entrySet().stream()
                    .map(entry -> new TagRankingItemInfo(entry.getValue(), entry.getKey()))
                    .collect(Collectors.toList());
            tagRankingRepository.increase7DaysTags(items);

            //when
            List<String> topK = tagRankingRepository.getTopKTagsFrom7Days(count);

            //then
            List<String> answer = countMap.values().stream()
                    .map(x -> String.valueOf(x)).collect(Collectors.toList());
            assertThat(answer.equals(topK)).isTrue();
        }

        @Test
        @DisplayName("태그 조회수 삭제 테스트")
        void deleteAll7DaysViewCountTest() {
            //given
            tagRankingRepository.increase7DaysTags(List.of(
                    new TagRankingItemInfo("aaa", 1L)
            ));

            //when
            tagRankingRepository.deleteAll7DaysTags();

            //then
            Cursor<TypedTuple<String>> cursor = redisTemplate.opsForZSet()
                    .scan(sevenDaysKey, ScanOptions.scanOptions().build());
            assertThat(cursor.hasNext()).isFalse();
        }
    }

    @Nested
    @DisplayName("특정 날짜 태그 조회수")
    class Day {

        LocalDateTime testTime = LocalDateTime.now();
        LocalDateTime testTime2 = LocalDateTime.now().minusDays(1);
        String testDayKey;
        String testDayKey2;

        @BeforeEach
        void init() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method privateMethod = TagRankingRepositoryRedisImpl.class
                    .getDeclaredMethod("getDayKey", LocalDateTime.class);
            privateMethod.setAccessible(true);
            testDayKey = (String)privateMethod.invoke(tagRankingRepository, testTime);
            testDayKey2 = (String)privateMethod.invoke(tagRankingRepository, testTime2);
        }

        @AfterEach
        void tearDown() {
            redisTemplate.delete(testDayKey);
            redisTemplate.delete(testDayKey2);
        }

        @Test
        @DisplayName("태그 배치 증가 테스트")
        void increaseViewCountTest() {
            //given
            List<TagRankingItemInfo> items = new ArrayList<>();
            int count = 100;
            for(int i = 0; i < count; i++) {
                items.add(new TagRankingItemInfo(String.valueOf(i), (long) i));
            }

            //when
            tagRankingRepository.increaseDayTags(testTime, items);
            tagRankingRepository.increaseDayTags(testTime, items);

            //then
            Cursor<Entry<String, String>> cursor = redisTemplate.<String, String>opsForHash().scan(
                    testDayKey, ScanOptions.scanOptions().count(count).build()
            );

            while(cursor.hasNext()) {
                Entry<String, String> next = cursor.next();
                assertThat(Long.parseLong(next.getValue())).
                        isEqualTo(Long.parseLong(next.getKey()) * 2);
            }
        }

        @Test
        @DisplayName("순회 테스트")
        void getDayViewCountIteratorTest() {
            //given
            List<TagRankingItemInfo> items = new ArrayList<>();
            int count = 100;
            for(int i = 0; i < count; i++) {
                items.add(new TagRankingItemInfo(String.valueOf(i), (long) i));
            }
            tagRankingRepository.increaseDayTags(testTime, items);

            //when
            Iterator<TagRankingItemInfo> iterator = tagRankingRepository.getDayTagIterator(
                    testTime
            );

            //then
            int iterateCount = 0;
            while(iterator.hasNext()) {
                iterateCount++;
                TagRankingItemInfo item = iterator.next();
                assertThat(Long.parseLong(item.getTag())).isEqualTo(item.getReferenceCount());
            }
            assertThat(count).isEqualTo(iterateCount);
        }

        @Test
        @DisplayName("특정 날짜의 조회수 전부 삭제 테스트")
        void deleteAllDayViewCountByTimeTest() {
            //given
            String keyword = "aaa";
            Long amount = 5L;

            tagRankingRepository.increaseDayTags(
                    testTime,
                    List.of(new TagRankingItemInfo(keyword, amount))
            );
            tagRankingRepository.increaseDayTags(
                    testTime2,
                    List.of(new TagRankingItemInfo(keyword, amount))
            );

            //when
            tagRankingRepository.deleteAllDayTagsByTime(testTime);

            //then
            String deleted = (String) redisTemplate.opsForHash().get(testDayKey, keyword);
            String notDeleted = (String) redisTemplate.opsForHash().get(testDayKey2, keyword);

            //지워져야 함
            assertThat(deleted).isEqualTo(null);
            //남아있어야 함
            assertThat(Long.parseLong(notDeleted)).isEqualTo(amount);
        }
    }
}