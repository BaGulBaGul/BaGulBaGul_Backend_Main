package com.BaGulBaGul.BaGulBaGul.global.batch.ranking;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.TagRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.repository.TagRankingRepository;
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
public class TagRankingUpdateJobConfig {

    private final String TAG_RANKING_UPDATE_JOB_NAME = "TAG_RANKING_UPDATE_JOB";
    private final String TAG_RANKING_UPDATE_STEP_NAME = "TAG_RANKING_UPDATE_STEP";
    private final String TAG_DAY_RANKING_DELETE_STEP_NAME = "TAG_DAY_RANKING_UPDATE_STEP";
    private final int CHUNK_SIZE = 100;

    private final TagRankingRepository tagRankingRepository;

    @Bean
    Job tagRankingUpdateJob(
            JobRepository jobRepository,
            Step tagRankingUpdateStep,
            Step deleteTagDayRankingStep
    ) {
        return new JobBuilder(TAG_RANKING_UPDATE_JOB_NAME)
                .repository(jobRepository)
                .start(tagRankingUpdateStep)
                .next(deleteTagDayRankingStep)
                .build();
    }

    //대상 날짜의 태그 점수만큼 7일간 태그 점수에서 감소시킴
    @JobScope
    @Bean
    Step tagRankingUpdateStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['targetDate']}") Date targetDate
    ) {
        LocalDateTime targetLocalDateTime = targetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        //targetDate의 모든 tag 점수를 읽음
        Iterator<TagRankingItemInfo> iterator = tagRankingRepository.getDayTagIterator(targetLocalDateTime);
        ItemReader<TagRankingItemInfo> reader = () -> {
            if(iterator.hasNext()) {
                return iterator.next();
            }
            return null;
        };

        //7일간 태그 점수에서 감소시킴
        ItemWriter<TagRankingItemInfo> writer = (items) -> {
            tagRankingRepository.decrease7DaysTags(items);
        };

        return new StepBuilder(TAG_RANKING_UPDATE_STEP_NAME)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .<TagRankingItemInfo, TagRankingItemInfo>chunk(CHUNK_SIZE)
                .reader(reader)
                .writer(writer)
                .build();
    }

    //대상 날짜의 태그 점수 삭제
    @JobScope
    @Bean
    Step deleteTagDayRankingStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['targetDate']}") Date targetDate
    ) {
        LocalDateTime targetLocalDateTime = targetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return new StepBuilder(TAG_DAY_RANKING_DELETE_STEP_NAME)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .tasklet((contribution, chunkContext) -> {
                    tagRankingRepository.deleteAllDayTagsByTime(targetLocalDateTime);
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
