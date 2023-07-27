package com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentResponseDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long> {

    // 모집글 단일 조회
    Optional<Recruitment> findById(Long id);

    // 특정 게시글의 모집글 조회 (정렬 기준 : (작성일, 좋아요)), dto 직접 조회
    @Query("select new com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.RecruitmentResponseDto.RInfoWithPaging(r.id, r.user.imageURL, r.title, r.content, r.startDate, r.endDate, r.tags, r.createdAt, r.lastModifiedAt) " +
            "from recruitment r " +
            "join recruitment_like rl " +
            "where r.post =: id and rl.recruitment = r group by r order by r.createdAt desc, count(r) desc")
    List<RecruitmentResponseDto.RInfoWithPaging> findRecruitmentByPostId(Long id, PageRequest pageRequest);

}
