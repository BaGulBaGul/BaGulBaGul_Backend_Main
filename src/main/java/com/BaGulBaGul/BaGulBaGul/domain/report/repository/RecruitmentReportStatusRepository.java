package com.BaGulBaGul.BaGulBaGul.domain.report.repository;

import com.BaGulBaGul.BaGulBaGul.domain.report.RecruitmentReportStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentReportStatusRepository extends JpaRepository<RecruitmentReportStatus, Long> {
    Optional<RecruitmentReportStatus> findByActiveRecruitmentId(Long recruitmentId);
}
