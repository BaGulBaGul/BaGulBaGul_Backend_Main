package com.BaGulBaGul.BaGulBaGul.domain.upload.service;

import java.util.List;

public interface TransactionResourceService {
    void deleteResourceAsyncAfterCommit(Long resourceId);
    void deleteResourcesAsyncAfterCommit(List<Long> resourceIds);
}
