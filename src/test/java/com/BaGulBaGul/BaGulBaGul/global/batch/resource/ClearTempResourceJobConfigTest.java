package com.BaGulBaGul.BaGulBaGul.global.batch.resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.BaGulBaGul.BaGulBaGul.domain.upload.Resource;
import com.BaGulBaGul.BaGulBaGul.domain.upload.S3TempResource;
import com.BaGulBaGul.BaGulBaGul.domain.upload.constant.StorageVendor;
import com.BaGulBaGul.BaGulBaGul.domain.upload.repository.ResourceRepository;
import com.BaGulBaGul.BaGulBaGul.domain.upload.repository.S3TempResourceRepository;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.ResourceService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.amazonaws.services.s3.AmazonS3;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@SpringBatchTest
@SpringBootTest
@ExtendWith(AllTestContainerExtension.class)
@ActiveProfiles("test")
class ClearTempResourceJobConfigTest {

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    S3TempResourceRepository s3TempResourceRepository;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @SpyBean
    ResourceService resourceService;

    @Value("${resource.temp.effective-minute}")
    private int TEMP_RESOURCE_EFFECTIVE_MINUTE;

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        @Primary
        Job testJob(@Qualifier("clearTempResourceJob") Job searchKeywordRankingUpdateJob) {
            return searchKeywordRankingUpdateJob;
        }
    }

    @TestConfiguration
    static class AmazonS3MockConfig{
        @Bean
        public AmazonS3 amazonS3() {
            return mock(AmazonS3.class);
        }
    }

    @AfterEach
    void tearDown() {
        s3TempResourceRepository.deleteAll();
        resourceRepository.deleteAll();
    }

    @Test
    void success() throws Exception {
        //given
        LocalDateTime startLocalDateTime = LocalDateTime.now();
        LocalDateTime expireTime = startLocalDateTime.minusMinutes(TEMP_RESOURCE_EFFECTIVE_MINUTE).minusMinutes(1);
        LocalDateTime notExpireTime = startLocalDateTime.minusMinutes(TEMP_RESOURCE_EFFECTIVE_MINUTE).plusMinutes(1);
        //만료된 임시 자원
        int expiredTempResourceCnt = 100;
        List<Long> expiredTempResourceList = new ArrayList<>();
        for(int i=0; i<expiredTempResourceCnt; i++) {
            TransactionStatus transactionStatus = platformTransactionManager.getTransaction(
                    new DefaultTransactionDefinition());
            Resource resource = resourceRepository.save(
                    Resource.builder()
                            .key("aaaa")
                            .storageVendor(StorageVendor.S3)
                            .uploadTime(expireTime)
                            .build()
            );
            s3TempResourceRepository.save(
                    S3TempResource.builder()
                            .resource(resource)
                            .uploadTime(expireTime)
                            .build()
            );
            platformTransactionManager.commit(transactionStatus);
            expiredTempResourceList.add(resource.getId());
        }
        //만료되지 않은 임시 자원
        int notExpiredTempResourceCnt = 30;
        List<Long> notExpiredTempResourceList = new ArrayList<>();
        for(int i=0; i<notExpiredTempResourceCnt; i++) {
            TransactionStatus transactionStatus = platformTransactionManager.getTransaction(
                    new DefaultTransactionDefinition());
            Resource resource = resourceRepository.save(
                    Resource.builder()
                            .key("aaaa")
                            .storageVendor(StorageVendor.S3)
                            .uploadTime(notExpireTime)
                            .build()
            );
            s3TempResourceRepository.save(
                    S3TempResource.builder()
                            .resource(resource)
                            .uploadTime(notExpireTime)
                            .build()
            );
            platformTransactionManager.commit(transactionStatus);
            notExpiredTempResourceList.add(resource.getId());
        }

        //when
        Random random = new Random();
        Date startDate = Date.from(startLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        JobParameters jobParameters = new JobParametersBuilder()
                .addDate("startTime", startDate)
                .addLong("unique", random.nextLong())
                .toJobParameters();
        jobLauncherTestUtils.launchJob(jobParameters);

        //then
        List<Long> resources = resourceRepository.findAll().stream()
                .map(Resource::getId).collect(Collectors.toList());
        resources.sort(Comparator.naturalOrder());
        List<Long> tempResources = s3TempResourceRepository.findAll().stream()
                .map(S3TempResource::getResourceId).collect(Collectors.toList());
        tempResources.sort(Comparator.naturalOrder());
        notExpiredTempResourceList.sort(Comparator.naturalOrder());
        //만료되지 않은 자원 = 남은 자원
        assertThat(notExpiredTempResourceList.equals(resources)).isEqualTo(true);
        //만료되지 않은 자원 = 남은 임시 자원
        assertThat(notExpiredTempResourceList.equals(tempResources)).isEqualTo(true);

        ArgumentCaptor<List<Long>> captor = ArgumentCaptor.forClass(List.class);
        verify(resourceService, atLeastOnce()).deleteResources(captor.capture());
        List<Long> deletedResources = captor.getAllValues().stream().flatMap(List::stream).collect(Collectors.toList());
        deletedResources.sort(Comparator.naturalOrder());
        expiredTempResourceList.sort(Comparator.naturalOrder());
        //만료된 자원 = 삭제 요청된 자원
        assertThat(expiredTempResourceList.equals(deletedResources)).isEqualTo(true);
    }
}