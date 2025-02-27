package com.BaGulBaGul.BaGulBaGul.global.batch.ranking;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.SearchKeywordRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.repository.SearchKeywordRankingRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SearchKeywordRankingUpdateJobConfig {

    private final String SEARCH_KEYWORD_RANKING_UPDATE_JOB_NAME = "SEARCH_KEYWORD_RANKING_UPDATE_JOB";
    private final String SEARCH_KEYWORD_RANKING_UPDATE_STEP_NAME = "SEARCH_KEYWORD_RANKING_UPDATE_STEP";
    private final String SEARCH_KEYWORD_DAY_RANKING_DELETE_STEP_NAME = "SEARCH_KEYWORD_DAY_RANKING_UPDATE_STEP";
    private final int CHUNK_SIZE = 100;

    private final SearchKeywordRankingRepository searchKeywordRankingRepository;

    @Bean
    Job searchKeywordRankingUpdateJob(
            JobRepository jobRepository,
            Step searchKeywordRankingUpdateStep,
            Step deleteSearchKeywordDayRankingStep
    ) {
        return new JobBuilder(SEARCH_KEYWORD_RANKING_UPDATE_JOB_NAME)
                .repository(jobRepository)
                .start(searchKeywordRankingUpdateStep)
                .next(deleteSearchKeywordDayRankingStep)
                .build();
    }

    //대상 날짜의 검색어 점수만큼 7일간 검색어 점수에서 감소시킴
    @JobScope
    @Bean
    Step searchKeywordRankingUpdateStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['targetDate']}") Date targetDate
    ) {
        LocalDateTime targetLocalDateTime = targetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        //targetDate의 모든 검색어 점수를 읽음
        Iterator<SearchKeywordRankingItemInfo> iterator = searchKeywordRankingRepository.getDayKeywordsIterator(targetLocalDateTime);
        ItemReader<SearchKeywordRankingItemInfo> reader = () -> {
            if(iterator.hasNext()) {
                return iterator.next();
            }
            return null;
        };

        //7일간 검색어 점수에서 감소시킴
        ItemWriter<SearchKeywordRankingItemInfo> writer = (items) -> {
            searchKeywordRankingRepository.decrease7DaysKeywords(items);
        };

        return new StepBuilder(SEARCH_KEYWORD_RANKING_UPDATE_STEP_NAME)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .<SearchKeywordRankingItemInfo, SearchKeywordRankingItemInfo>chunk(CHUNK_SIZE)
                .reader(reader)
                .writer(writer)
                .build();
    }

    //대상 날짜의 검색어 점수 삭제
    @JobScope
    @Bean
    Step deleteSearchKeywordDayRankingStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['targetDate']}") Date targetDate
    ) {
        LocalDateTime targetLocalDateTime = targetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return new StepBuilder(SEARCH_KEYWORD_DAY_RANKING_DELETE_STEP_NAME)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .tasklet((contribution, chunkContext) -> {
                    searchKeywordRankingRepository.deleteAllDayKeywordsByTime(targetLocalDateTime);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
