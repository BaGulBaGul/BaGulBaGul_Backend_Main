package com.BaGulBaGul.BaGulBaGul.global.batch.resource;

import com.BaGulBaGul.BaGulBaGul.domain.upload.S3TempResource;
import com.BaGulBaGul.BaGulBaGul.domain.upload.repository.ResourceRepository;
import com.BaGulBaGul.BaGulBaGul.domain.upload.repository.S3TempResourceRepository;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.ResourceService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ClearTempResourceJobConfig {

    private final String CLEAR_TEMP_RESOURCE_JOB_NAME = "CLEAR_TEMP_RESOURCE_JOB";
    private final String CLEAR_TEMP_RESOURCE_STEP_NAME = "CLEAR_TEMP_RESOURCE_STEP";
    private final String CLEAR_TEMP_RESOURCE_READER_NAME = "CLEAR_TEMP_RESOURCE_READER";
    private final int CHUNK_SIZE = 20;
    @Value("${resource.temp.effective-minute}")
    private int TEMP_RESOURCE_EFFECTIVE_MINUTE;


    private final ResourceService resourceService;


    @Bean
    Job clearTempResourceJob(
            JobRepository jobRepository,
            Step clearTempResourceStep
    ) {
        return new JobBuilder(CLEAR_TEMP_RESOURCE_JOB_NAME)
                .repository(jobRepository)
                .start(clearTempResourceStep)
                .build();
    }

    @Bean
    @JobScope
    Step clearTempResourceStep(
            JobRepository jobRepository,
            PlatformTransactionManager platformTransactionManager,
            ItemReader<Long> clearTempResourceReader,
            ItemWriter<Long> clearTempResourceWriter
    ) {
        return new StepBuilder(CLEAR_TEMP_RESOURCE_STEP_NAME)
                .repository(jobRepository)
                .transactionManager(platformTransactionManager)
                .<Long, Long>chunk(CHUNK_SIZE)
                .reader(clearTempResourceReader)
                .writer(clearTempResourceWriter)
                .build();
    }

    @Bean
    @StepScope
    JpaPagingItemReader<Long> clearTempResourceReader(
            EntityManagerFactory entityManagerFactory,
            @Value("#{jobParameters['startTime']}") Date startTime
    ) {
        LocalDateTime startLocalDateTime = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime expireTime = startLocalDateTime.minusMinutes(TEMP_RESOURCE_EFFECTIVE_MINUTE);

        JpaPagingItemReader<Long> reader = new JpaPagingItemReader<>(){
            @Override
            public int getPage() {
                return 0;
            }
        };
        reader.setName(CLEAR_TEMP_RESOURCE_READER_NAME);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString("SELECT tr.resourceId FROM S3TempResource tr WHERE tr.uploadTime < :expireTime");
        reader.setParameterValues(Collections.singletonMap("expireTime", expireTime));
        reader.setPageSize(CHUNK_SIZE);
        return reader;
    }

    @Bean
    @StepScope
    ItemWriter<Long> clearTempResourceWriter() {
        return items -> resourceService.deleteTempResources((List<Long>) items);
    }
}
