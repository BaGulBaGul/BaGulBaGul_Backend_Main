package com.BaGulBaGul.BaGulBaGul.domain.upload.service;

import com.BaGulBaGul.BaGulBaGul.domain.upload.exception.NotImageException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageUploadServiceImpl implements ImageUploadService {
    private final String IMAGE_PATH = "image";
    private final Set<String> IMAGE_EXT = new HashSet<>(List.of(
            ".jpg",".png",".gif"
    ));
    private final ResourceService resourceService;

    @Override
    public Long uploadImage(MultipartFile multipartFile) throws NotImageException, IOException {
        String ext = getFileExtention(multipartFile.getOriginalFilename());
        if(!IMAGE_EXT.contains(ext.toLowerCase())){
            throw new NotImageException();
        }

        return resourceService.uploadResource(IMAGE_PATH, multipartFile);
    }

    private String getFileExtention(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
