package com.BaGulBaGul.BaGulBaGul.global.upload.repository;

import com.BaGulBaGul.BaGulBaGul.global.upload.Resource;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    @Query(value = "SELECT r FROM Resource r WHERE r.id in :ids")
    List<Resource> findAllByIds(@Param(value = "ids") List<Long> ids);
}
