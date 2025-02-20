package com.BaGulBaGul.BaGulBaGul.domain.upload.service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

public abstract class ResourceService {

    public abstract Long uploadResource(String path, MultipartFile multipartFile) throws IOException;
    public abstract void deleteResource(Long resourceId);
    public abstract void deleteResources(List<Long> resourceIds);
    public abstract void deleteResourceAsync(Long resourceId);
    public abstract void deleteResourcesAsync(List<Long> resourceIds);
    public abstract String getResourceUrlFromId(Long resourceId);
    public abstract List<String> getResourceUrlsFromIds(List<Long> resourceIds);
    public abstract void cancelTempResource(Long resourceId);
    public abstract void cancelTempResources(List<Long> resourceIds);

    protected String createKey(String path, String uploadedName) {
        return path + "/" + createRandomFileName(uploadedName);
    }

    protected String createRandomFileName(String fileName) {
        String ext = getFileExtention(fileName);
        return UUID.randomUUID().toString() + ext;
    }

    private String getFileExtention(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
