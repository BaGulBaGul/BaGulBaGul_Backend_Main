package com.BaGulBaGul.BaGulBaGul.domain.report.repository;

import com.BaGulBaGul.BaGulBaGul.domain.report.EventReportStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventReportStatusRepository extends JpaRepository<EventReportStatus, Long> {
    Optional<EventReportStatus> findByActiveEventId(Long eventId);
}
