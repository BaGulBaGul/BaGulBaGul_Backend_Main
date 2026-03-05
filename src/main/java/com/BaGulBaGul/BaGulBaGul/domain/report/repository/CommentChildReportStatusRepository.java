package com.BaGulBaGul.BaGulBaGul.domain.report.repository;

import com.BaGulBaGul.BaGulBaGul.domain.report.CommentChildReportStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentChildReportStatusRepository extends JpaRepository<CommentChildReportStatus, Long> {
    Optional<CommentChildReportStatus> findByActivePostCommentChildId(Long commentChildId);
}
