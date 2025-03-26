package com.BaGulBaGul.BaGulBaGul.domain.upload.repository;

import com.BaGulBaGul.BaGulBaGul.domain.upload.S3TempResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3TempResourceRepository extends JpaRepository<S3TempResource, Long> {
}
