package com.BaGulBaGul.BaGulBaGul.domain.upload.service;

import java.util.Collection;
import java.util.List;

public interface TransactionResourceService {
    void deleteResourceAsyncAfterCommit(Long resourceId);
    void deleteResourcesAsyncAfterCommit(Collection<Long> resourceIds);
}
