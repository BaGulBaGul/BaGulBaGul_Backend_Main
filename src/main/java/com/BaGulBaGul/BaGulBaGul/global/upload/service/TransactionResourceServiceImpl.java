package com.BaGulBaGul.BaGulBaGul.global.upload.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class TransactionResourceServiceImpl implements TransactionResourceService{

    private final ResourceService resourceService;

    @Override
    public void deleteResourceAsyncAfterCommit(Long resourceId) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                resourceService.deleteResourceAsync(resourceId);
            }
        });
    }

    @Override
    public void deleteResourcesAsyncAfterCommit(List<Long> resourceIds) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                resourceService.deleteResourcesAsync(resourceIds);
            }
        });
    }
}
