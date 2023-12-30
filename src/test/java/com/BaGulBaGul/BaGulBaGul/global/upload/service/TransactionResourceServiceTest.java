package com.BaGulBaGul.BaGulBaGul.global.upload.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class TransactionResourceServiceTest {

    @MockBean
    ResourceService resourceService;
    @Autowired
    TransactionResourceService transactionResourceService;

    @Nested
    @DisplayName("deleteResourceAsyncAfterCommit 메서드 테스트")
    class deleteResourceAsyncAfterCommitTest{
        @Test
        @DisplayName("트랜젝션 커밋 후에 deleteResourceAsync를 호출해야 함")
        @Transactional
        void shouldDeleteResourceAsync_AfterCommit() {
            transactionResourceService.deleteResourceAsyncAfterCommit(null);
            TestTransaction.flagForCommit();
            TestTransaction.end();
            verify(resourceService, atLeastOnce()).deleteResourceAsync(any());
        }

        @Test
        @DisplayName("트랜젝션 롤백 후에 deleteResourceAsync를 호출하지 말아야 함")
        @Transactional
        void shouldNotDeleteResourceAsync_AfterRollback() {
            transactionResourceService.deleteResourceAsyncAfterCommit(null);
            TestTransaction.flagForRollback();
            TestTransaction.end();
            verify(resourceService, never()).deleteResourceAsync(any());
        }
    }
    @Nested
    @DisplayName("deleteResourcesAsyncAfterCommit 메서드 테스트")
    class deleteResourcesAsyncAfterCommitTest{
        @Test
        @DisplayName("트랜젝션 커밋 후에 deleteResourcesAsync를 호출해야 함")
        @Transactional
        void shouldDeleteResourcesAsync_AfterCommit() {
            transactionResourceService.deleteResourcesAsyncAfterCommit(null);
            TestTransaction.flagForCommit();
            TestTransaction.end();
            verify(resourceService, atLeastOnce()).deleteResourcesAsync(any());

        }

        @Test
        @DisplayName("트랜젝션 롤백 후에 deleteResourcesAsync를 호출하지 말아야 함")
        @Transactional
        void shouldNotDeleteResourcesAsync_AfterRollback() {
            transactionResourceService.deleteResourcesAsyncAfterCommit(null);
            TestTransaction.flagForRollback();
            TestTransaction.end();
            verify(resourceService, never()).deleteResourcesAsync(any());
        }
    }
}