package com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.RecruitmentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentLikeRepository extends JpaRepository<RecruitmentLike, Long> {

    // 좋아요 개수 조회
    Integer countByRecruitmentId(Long id);
}
