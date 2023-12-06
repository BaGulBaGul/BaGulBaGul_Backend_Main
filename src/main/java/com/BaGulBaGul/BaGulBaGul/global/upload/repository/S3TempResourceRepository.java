package com.BaGulBaGul.BaGulBaGul.global.upload.repository;

import com.BaGulBaGul.BaGulBaGul.global.upload.S3TempResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3TempResourceRepository extends JpaRepository<S3TempResource, Long> {
}
