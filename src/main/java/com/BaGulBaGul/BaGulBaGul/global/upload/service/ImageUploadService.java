package com.BaGulBaGul.BaGulBaGul.global.upload.service;

import com.BaGulBaGul.BaGulBaGul.global.upload.exception.NotImageException;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageUploadService {
    String uploadImage(MultipartFile multipartFile) throws NotImageException, IOException;
}
