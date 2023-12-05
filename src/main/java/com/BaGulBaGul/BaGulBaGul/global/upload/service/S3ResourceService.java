package com.BaGulBaGul.BaGulBaGul.global.upload.service;

import com.BaGulBaGul.BaGulBaGul.global.upload.S3TempResource;
import com.BaGulBaGul.BaGulBaGul.global.upload.repository.S3TempResourceRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3ResourceService extends ResourceService {

    private final AmazonS3 amazonS3;
    private final S3TempResourceRepository s3TempResourceRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadResource(String path, MultipartFile multipartFile) throws IOException {
        String key = createKey(path, multipartFile.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        PutObjectResult qq;

        qq = amazonS3.putObject(bucketName, key, multipartFile.getInputStream(), metadata);

        s3TempResourceRepository.save(
                S3TempResource.builder()
                        .key(key)
                        .uploadTime(LocalDateTime.now())
                        .build()
        );
        return key;
    }

    @Override
    public void deleteResource(String key) {
        amazonS3.deleteObject(bucketName, key);
    }

    @Override
    @Async
    public void deleteResourcesAsync(List<String> keys) {
        if(keys == null)
            return;
        for(String key : keys) {
            amazonS3.deleteObject(bucketName, key);
        }
    }

    @Override
    public String getResourceUrlFromKey(String key) {
        return amazonS3.getUrl(bucketName, key).toString();
    }
}
