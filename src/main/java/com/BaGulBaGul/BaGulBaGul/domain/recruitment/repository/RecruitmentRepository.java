package com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    // 모집글 단일 조회
    Optional<Recruitment> findById(Long id);

    // 특정 게시글의 모집글 조회 (정렬기준 : 작성일)
    List<Recruitment> findRecruitmentByPostIdOrderByCreatedAtDesc(Long id, PageRequest pageRequest);


}
