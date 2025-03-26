package com.BaGulBaGul.BaGulBaGul.domain.upload.service;

import com.BaGulBaGul.BaGulBaGul.domain.upload.exception.NotImageException;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
    Long uploadImage(MultipartFile multipartFile) throws NotImageException, IOException;
}
