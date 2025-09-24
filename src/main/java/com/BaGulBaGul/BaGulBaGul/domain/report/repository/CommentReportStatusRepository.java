package com.BaGulBaGul.BaGulBaGul.domain.report.repository;

import com.BaGulBaGul.BaGulBaGul.domain.report.CommentChildReportStatus;
import com.BaGulBaGul.BaGulBaGul.domain.report.CommentReportStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentReportStatusRepository extends JpaRepository<CommentReportStatus, Long> {
    Optional<CommentReportStatus> findByActivePostCommentId(Long commentId);
}
