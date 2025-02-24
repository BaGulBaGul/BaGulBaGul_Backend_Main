package com.BaGulBaGul.BaGulBaGul.global.batch.ranking;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.EventViewsRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.repository.EventViewRankingRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class EventViewsRankingUpdateJobConfig {

    private final String EVENT_VIEW_RANKING_UPDATE_JOB_NAME = "EVENT_VIEW_RANKING_UPDATE_JOB";
    private final String EVENT_VIEW_RANKING_UPDATE_STEP_NAME = "EVENT_VIEW_RANKING_UPDATE_STEP";
    private final String EVENT_VIEW_DAY_RANKING_DELETE_STEP_NAME = "EVENT_VIEW_DAY_RANKING_UPDATE_STEP";
    private final int CHUNK_SIZE = 100;

    private final EventViewRankingRepository eventViewRankingRepository;


    @Bean
    Job eventViewsRankingUpdateJob(
            JobRepository jobRepository,
            Step festivalViewsRankingUpdateStep,
            Step localEventViewsRankingUpdateStep,
            Step partyViewsRankingUpdateStep,
            Step deleteDayViewRankingStep
    ) {
        return new JobBuilder(EVENT_VIEW_RANKING_UPDATE_JOB_NAME)
                .repository(jobRepository)
                .start(festivalViewsRankingUpdateStep)
                .next(localEventViewsRankingUpdateStep)
                .next(partyViewsRankingUpdateStep)
                .next(deleteDayViewRankingStep)
                .build();
    }

    @Bean
    @JobScope
    Step festivalViewsRankingUpdateStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['targetDate']}") Date targetDate
    ) {
        return getViewRankingUpdateStep(
                jobRepository,
                transactionManager,
                EventType.FESTIVAL,
                targetDate
        );
    }

    @Bean
    @JobScope
    Step localEventViewsRankingUpdateStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['targetDate']}") Date targetDate
    ) {
        return getViewRankingUpdateStep(
                jobRepository,
                transactionManager,
                EventType.LOCAL_EVENT,
                targetDate
        );
    }

    @Bean
    @JobScope
    Step partyViewsRankingUpdateStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['targetDate']}") Date targetDate
    ) {
        return getViewRankingUpdateStep(
                jobRepository,
                transactionManager,
                EventType.PARTY,
                targetDate
        );
    }

    @Bean
    @JobScope
    Step deleteDayViewRankingStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Value("#{jobParameters['targetDate']}") Date targetDate
    ) {
        LocalDateTime startLocalDateTime = targetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return new StepBuilder(EVENT_VIEW_DAY_RANKING_DELETE_STEP_NAME)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .tasklet((contribution, chunkContext) -> {
                    eventViewRankingRepository.deleteAllDayViewCountByTime(EventType.FESTIVAL, startLocalDateTime);
                    eventViewRankingRepository.deleteAllDayViewCountByTime(EventType.LOCAL_EVENT, startLocalDateTime);
                    eventViewRankingRepository.deleteAllDayViewCountByTime(EventType.PARTY, startLocalDateTime);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    private Step getViewRankingUpdateStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            EventType eventType,
            Date targetDate
    ) {
        LocalDateTime startLocalDateTime = targetDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        ItemReader<EventViewsRankingItemInfo> reader = new EventViewsRankingItemReader(
                eventViewRankingRepository,
                eventType,
                startLocalDateTime
        );

        ItemWriter<EventViewsRankingItemInfo> writer = new EventViewsRankingItemWriter(
                eventViewRankingRepository,
                eventType
        );

        return new StepBuilder(EVENT_VIEW_RANKING_UPDATE_STEP_NAME)
                .repository(jobRepository)
                .transactionManager(transactionManager)
                .<EventViewsRankingItemInfo, EventViewsRankingItemInfo>chunk(CHUNK_SIZE)
                .reader(reader)
                .writer(writer)
                .build();
    }
}