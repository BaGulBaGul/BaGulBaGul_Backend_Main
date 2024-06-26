package com.BaGulBaGul.BaGulBaGul.global.upload.service;

import java.util.List;

public interface TransactionResourceService {
    void deleteResourceAsyncAfterCommit(Long resourceId);
    void deleteResourcesAsyncAfterCommit(List<Long> resourceIds);
}
