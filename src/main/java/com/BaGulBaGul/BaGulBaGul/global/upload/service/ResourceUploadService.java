package com.BaGulBaGul.BaGulBaGul.global.upload.service;

import com.BaGulBaGul.BaGulBaGul.global.upload.exception.NotImageException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public abstract class ResourceUploadService {

    private final String IMAGE_PATH = "image";
    private final Set<String> IMAGE_EXT = new HashSet<>(List.of(
            ".jpg",".png",".gif"
    ));

    public abstract String uploadResource(String path, MultipartFile multipartFile) throws IOException;
    public abstract void deleteResource(String key);
    public abstract void deleteResourcesAsync(List<String> keys);
    public abstract String getResourceUrlFromKey(String key);

    public String uploadImage(MultipartFile multipartFile) throws NotImageException, IOException {
        String ext = getFileExtention(multipartFile.getOriginalFilename());
        if(!IMAGE_EXT.contains(ext.toLowerCase())){
            throw new NotImageException();
        }

        return uploadResource(IMAGE_PATH, multipartFile);
    }

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
