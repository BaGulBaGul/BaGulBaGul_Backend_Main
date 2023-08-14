package com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.RecruitmentCommentChild;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecruitmentCommentChildRepository extends JpaRepository<RecruitmentCommentChild, Long> {

    // 특정 댓글의 대댓글 조회
    List<RecruitmentCommentChild> findCommentChildByRecruitmentCommentIdOrderByCreatedAt(Long id, PageRequest pageRequest);
}
