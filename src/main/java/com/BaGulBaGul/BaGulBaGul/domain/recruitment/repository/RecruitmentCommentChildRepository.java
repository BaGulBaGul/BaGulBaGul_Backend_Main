package com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.RecruitmentCommentChild;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitmentCommentChildRepository extends JpaRepository<RecruitmentCommentChild, Long> {

    // 특정 댓글의 대댓글 조회
    List<RecruitmentCommentChild> findCommentChildByRecruitmentCommentId(Long id, PageRequest pageRequest);
}
