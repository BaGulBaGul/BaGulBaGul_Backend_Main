package com.BaGulBaGul.BaGulBaGul.domain.report.repository;

import com.BaGulBaGul.BaGulBaGul.domain.report.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReportStatusRepository extends JpaRepository<ReportStatus, Long> {
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update ReportStatus rs "
            + "set rs.totalReportCount = rs.totalReportCount + 1, "
                + "rs.notRelevantReportCount = rs.notRelevantReportCount + 1 "
            + "where rs.id = :reportStatusId")
    void increaseNotRelevantReportCount(@Param(value = "reportStatusId") Long reportStatusId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update ReportStatus rs "
            + "set rs.totalReportCount = rs.totalReportCount + 1, "
            + "rs.offensiveContentReportCount = rs.offensiveContentReportCount + 1 "
            + "where rs.id = :reportStatusId")
    void increaseOffensiveContentReportCount(@Param(value = "reportStatusId") Long reportStatusId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update ReportStatus rs "
            + "set rs.totalReportCount = rs.totalReportCount + 1, "
            + "rs.defamatoryReportCount = rs.defamatoryReportCount + 1 "
            + "where rs.id = :reportStatusId")
    void increaseDefamatoryReportCount(@Param(value = "reportStatusId") Long reportStatusId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update ReportStatus rs "
            + "set rs.totalReportCount = rs.totalReportCount + 1, "
            + "rs.ectReportCount = rs.ectReportCount + 1 "
            + "where rs.id = :reportStatusId")
    void increaseEctReportCount(@Param(value = "reportStatusId") Long reportStatusId);
}
