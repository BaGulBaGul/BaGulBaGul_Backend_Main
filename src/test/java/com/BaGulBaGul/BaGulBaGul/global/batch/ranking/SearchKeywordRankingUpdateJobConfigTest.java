package com.BaGulBaGul.BaGulBaGul.global.batch.ranking;

import static org.assertj.core.api.Assertions.assertThat;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.SearchKeywordRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.TagRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.repository.SearchKeywordRankingRepository;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.repository.TagRankingRepository;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.context.ActiveProfiles;

@SpringBatchTest
@SpringBootTest
@ExtendWith(AllTestContainerExtension.class)
@ActiveProfiles("test")
class SearchKeywordRankingUpdateJobConfigTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    SearchKeywordRankingRepository searchKeywordRankingRepository;

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        @Primary
        Job testJob(@Qualifier("searchKeywordRankingUpdateJob") Job searchKeywordRankingUpdateJob) {
            return searchKeywordRankingUpdateJob;
        }
    }

    LocalDateTime targetLocalDateTime = LocalDateTime.now();
    Date targetDate = Date.from(targetLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
    LocalDateTime notToDeleteTime = targetLocalDateTime.minusDays(1L);

    @AfterEach
    void tearDown() {
        searchKeywordRankingRepository.deleteAllDayKeywordsByTime(targetLocalDateTime);
        searchKeywordRankingRepository.deleteAllDayKeywordsByTime(notToDeleteTime);
        searchKeywordRankingRepository.deleteAll7DaysKeywords();
    }

    @Test
    @DisplayName("성공")
    void success() throws Exception {
        //given
        //반영 대상 데이터
        int eventCount = 200;
        List<SearchKeywordRankingItemInfo> items = new ArrayList<>();
        for(int i = 0; i < eventCount; i++) {
            items.add(new SearchKeywordRankingItemInfo(String.valueOf(i), (long) i));
        }
        searchKeywordRankingRepository.increaseDayKeywords(targetLocalDateTime, items);
        //영향받지 않을 날짜의 데이터 1개
        searchKeywordRankingRepository.increaseDayKeywords(notToDeleteTime, items.subList(0, 1));

        //when
        Random random = new Random();
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("targetDate", targetDate)
                .addLong("unique", random.nextLong())
                .toJobParameters();
        jobLauncherTestUtils.launchJob(jobParameters);

        //then
        //targetDate의 모든 조회수가 최근 7일 조회수에 -로 반영
        List<String> topK = searchKeywordRankingRepository.getTopKKeywordsFrom7Days(eventCount);
        for(int i=0; i<eventCount; i++) {
            assertThat(Long.parseLong(topK.get(i))).isEqualTo(i);
        }
        //targetDate의 모든 조회수 삭제
        Iterator<SearchKeywordRankingItemInfo> iterator = searchKeywordRankingRepository.getDayKeywordsIterator(
                targetLocalDateTime);
        assertThat(iterator.hasNext()).isFalse();
        //notToDeleteTime의 조회수는 삭제되면 안됨
        iterator = searchKeywordRankingRepository.getDayKeywordsIterator(notToDeleteTime);
        assertThat(iterator.hasNext()).isTrue();
    }
}