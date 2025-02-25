package com.BaGulBaGul.BaGulBaGul.global.batch.ranking;

import static org.assertj.core.api.Assertions.assertThat;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.EventViewsRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.repository.EventViewRankingRepository;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.repository.EventViewRankingRepositoryRedisImpl;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.extension.MysqlTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.extension.RedisTestContainerExtension;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBatchTest
@SpringBootTest
@ExtendWith(AllTestContainerExtension.class)
@ActiveProfiles("test")
class EventViewsRankingUpdateJobConfigTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    EventViewRankingRepository eventViewRankingRepository;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        @Primary
        Job testJob(@Qualifier("eventViewsRankingUpdateJob") Job eventViewsRankingUpdateJob) {
            return eventViewsRankingUpdateJob;
        }
    }

    LocalDateTime targetLocalDateTime = LocalDateTime.now();
    Date targetDate = Date.from(targetLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

    @AfterEach
    void tearDown() {
        for(EventType eventType : EventType.values()) {
            eventViewRankingRepository.deleteAllDayViewCountByTime(eventType, targetLocalDateTime);
            eventViewRankingRepository.deleteAll7DaysViewCount(eventType);
        }
    }

    @ParameterizedTest
    @EnumSource(EventType.class)
    @DisplayName("성공")
    void success(EventType eventType) throws Exception {
        //given
        //반영 대상 데이터
        int eventCount = 200;
        for(int i = 0; i < eventCount; i++) {
            eventViewRankingRepository.increaseDayViewCount(
                    (long) i,
                    eventType,
                    targetLocalDateTime,
                    (long) i
            );
        }
        //영향받지 않을 날짜의 데이터 1개
        LocalDateTime notToDeleteTime = targetLocalDateTime.minusDays(1L);
        eventViewRankingRepository.increaseDayViewCount(
                1L,
                eventType,
                notToDeleteTime,
                1L
        );

        //when
        Random random = new Random();
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("targetDate", targetDate)
                .addLong("unique", random.nextLong())
                .toJobParameters();
        jobLauncherTestUtils.launchJob(jobParameters);

        //then
        //targetDate의 모든 조회수가 최근 7일 조회수에 -로 반영
        List<Long> topK = eventViewRankingRepository.getTopKRankEventFrom7DaysViewCount(
                eventType,
                eventCount
        );
        for(int i=0; i<eventCount; i++) {
            assertThat(topK.get(i)).isEqualTo(i);
        }
        //targetDate의 모든 조회수 삭제
        Iterator<EventViewsRankingItemInfo> iterator = eventViewRankingRepository.getDayViewCountIterator(eventType,
                targetLocalDateTime);
        assertThat(iterator.hasNext()).isFalse();
        //notToDeleteTime의 조회수는 삭제되면 안됨
        iterator = eventViewRankingRepository.getDayViewCountIterator(eventType,
                notToDeleteTime);
        assertThat(iterator.hasNext()).isTrue();
    }
}