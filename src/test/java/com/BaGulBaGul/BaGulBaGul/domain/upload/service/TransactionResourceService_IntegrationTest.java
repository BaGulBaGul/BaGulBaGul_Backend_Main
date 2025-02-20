package com.BaGulBaGul.BaGulBaGul.domain.upload.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.BaGulBaGul.BaGulBaGul.domain.upload.service.ResourceService;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.TransactionResourceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class TransactionResourceService_IntegrationTest {

    @MockBean
    ResourceService resourceService;
    @Autowired
    TransactionResourceService transactionResourceService;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Nested
    @DisplayName("deleteResourceAsyncAfterCommit 메서드 테스트")
    class deleteResourceAsyncAfterCommitTest{
        @Test
        @DisplayName("트랜젝션 커밋 후에 deleteResourceAsync를 호출해야 함")
        void shouldDeleteResourceAsync_AfterCommit() {
            //given
            //when
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            transactionResourceService.deleteResourceAsyncAfterCommit(null);
            transactionManager.commit(status);
            //then
            verify(resourceService, atLeastOnce()).deleteResourceAsync(any());
        }

        @Test
        @DisplayName("트랜젝션 롤백 후에 deleteResourceAsync를 호출하지 말아야 함")
        void shouldNotDeleteResourceAsync_AfterRollback() {
            //given
            //when
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            transactionResourceService.deleteResourceAsyncAfterCommit(null);
            transactionManager.rollback(status);
            //then
            verify(resourceService, never()).deleteResourceAsync(any());
        }
    }
    @Nested
    @DisplayName("deleteResourcesAsyncAfterCommit 메서드 테스트")
    class deleteResourcesAsyncAfterCommitTest{
        @Test
        @DisplayName("트랜젝션 커밋 후에 deleteResourcesAsync를 호출해야 함")
        void shouldDeleteResourcesAsync_AfterCommit() {
            //given
            //when
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            transactionResourceService.deleteResourcesAsyncAfterCommit(null);
            transactionManager.commit(status);
            //then
            verify(resourceService, atLeastOnce()).deleteResourcesAsync(any());
        }

        @Test
        @DisplayName("트랜젝션 롤백 후에 deleteResourcesAsync를 호출하지 말아야 함")
        void shouldNotDeleteResourcesAsync_AfterRollback() {
            //given
            //when
            TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
            transactionResourceService.deleteResourcesAsyncAfterCommit(null);
            transactionManager.rollback(status);
            //then
            verify(resourceService, never()).deleteResourcesAsync(any());
        }
    }
}