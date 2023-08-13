package com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.RecruitmentComment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruitmentCommentRepository extends JpaRepository<RecruitmentComment, Long> {

    // 모집글의 댓글 조회
    List<RecruitmentComment> findCommentByRecruitmentIdOrderByCreatedAtDesc(Long id, PageRequest pageRequest);

    // 모집글의 댓글 개수 조회
    Integer countByRecruitmentId(Long id);
}
