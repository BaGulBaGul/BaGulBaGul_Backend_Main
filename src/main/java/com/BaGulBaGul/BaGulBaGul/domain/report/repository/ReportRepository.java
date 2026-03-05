package com.BaGulBaGul.BaGulBaGul.domain.report.repository;

import com.BaGulBaGul.BaGulBaGul.domain.report.Report;
import com.BaGulBaGul.BaGulBaGul.domain.report.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
    @EntityGraph(attributePaths = {"reportedUser", "reportingUser"})
    Page<Report> findByReportStatus(ReportStatus reportStatus, Pageable pageable);
}
