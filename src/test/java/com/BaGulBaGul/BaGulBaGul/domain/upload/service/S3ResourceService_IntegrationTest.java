package com.BaGulBaGul.BaGulBaGul.domain.upload.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import com.BaGulBaGul.BaGulBaGul.domain.upload.Resource;
import com.BaGulBaGul.BaGulBaGul.domain.upload.constant.StorageVendor;
import com.BaGulBaGul.BaGulBaGul.domain.upload.repository.ResourceRepository;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.S3ResourceService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.extension.MysqlTestContainerExtension;
import com.amazonaws.services.s3.AmazonS3;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class S3ResourceService_IntegrationTest {
    @MockBean
    AmazonS3 amazonS3;
    @Autowired
    ResourceRepository resourceRepository;
    @Autowired
    S3ResourceService resourceService;

    @Autowired
    PlatformTransactionManager transactionManager;

    @BeforeEach
    void init() {
        //효율성을 위해 일단 @DirtiesContext보단 reset으로 처리했다.
        reset(amazonS3);
    }

    //@Nested + @TestConfiguration + @Transactional 조합은 @Transactional의 autocommit=false가 작동 안하는 버그가 있는듯.
    //@Nested + @Transactional 혹은 @TestConfiguration + @Transactional 조합은 작동 함
    //가급적이면 TransactionManager를 통해 직접 제어.
    @Nested
    @DisplayName("deleteResource 메서드 테스트")
    class deleteResourceTest {
        Long resourceId;
        @BeforeEach
        void init() {
            resourceId = resourceRepository.save(
                    Resource.builder()
                            .key("test")
                            .uploadTime(LocalDateTime.now())
                            .storageVendor(StorageVendor.S3)
                            .build()
            ).getId();
        }
        @AfterEach
        void clear() {
            resourceRepository.deleteAll();
        }

        @Test
        @DisplayName("트랜젝션 커밋 후에 amazonS3.deleteObject를 호출해야 함")
        void shouldDeleteObject_AfterCommit() {
            //given
            //when
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            resourceService.deleteResource(resourceId);
            transactionManager.commit(status);
            //then
            verify(amazonS3, atLeastOnce()).deleteObject(any(), any());
        }

        @Test
        @DisplayName("트랜젝션 롤백 후에 amazonS3.deleteObject를 호출하지 말아야 함")
        void shouldNotDeleteObject_AfterRollback() {
            //given
            //when
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            resourceService.deleteResource(resourceId);
            transactionManager.rollback(status);
            //then
            verify(amazonS3, never()).deleteObject(any(), any());
        }
    }
    @Nested
    @DisplayName("deleteResources 메서드 테스트")
    class deleteResourcesTest {
        List<Long> resourceIds = new ArrayList<>();
        @BeforeEach
        void init() {
            resourceIds.add(resourceRepository.save(
                    Resource.builder()
                            .key("test")
                            .uploadTime(LocalDateTime.now())
                            .storageVendor(StorageVendor.S3)
                            .build()
            ).getId());
        }
        @AfterEach
        void clear() {
            resourceRepository.deleteAll();
        }
        @Test
        @DisplayName("트랜젝션 커밋 후에 amazonS3.deleteObject를 호출해야 함")
        void shouldDeleteObject_AfterCommit() {
            //given
            //when
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            resourceService.deleteResources(resourceIds);
            transactionManager.commit(status);
            //then
            verify(amazonS3, atLeastOnce()).deleteObject(any(), any());
        }

        @Test
        @DisplayName("트랜젝션 롤백 후에 amazonS3.deleteObject를 호출하지 말아야 함")
        void shouldNotDeleteObject_AfterRollback() {
            //given
            //when
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            resourceService.deleteResources(resourceIds);
            transactionManager.rollback(status);
            //then
            verify(amazonS3, never()).deleteObject(any(), any());
        }
    }
}