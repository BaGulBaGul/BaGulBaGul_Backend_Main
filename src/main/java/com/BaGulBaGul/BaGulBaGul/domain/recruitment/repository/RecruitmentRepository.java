package com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.repository.querydsl.FindRecruitmentByCondition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, FindRecruitmentByCondition {
    @EntityGraph(attributePaths = {"post.user"})
    Optional<Recruitment> findWithPostAndUserById(Long recruitmentId);

    @Query(
            value = "SELECT r FROM Recruitment r INNER JOIN FETCH r.post p INNER JOIN FETCH p.user WHERE r.id in :recruitmentIds"
    )
    List<Recruitment> findWithPostAndUserByIds(@Param("recruitmentIds") List<Long> recruitmentIds);

    @Query(
            value = "SELECT r FROM Recruitment r INNER JOIN r.post p INNER JOIN p.likes pl WHERE pl.user.id = :userId"
    )
    Page<Recruitment> getLikeRecruitmentByUser(
            @Param("userId") Long userId, Pageable pageable
    );
    @Query(
            value = "SELECT r FROM Recruitment r INNER JOIN FETCH r.post where r.id in :ids"
    )
    List<Recruitment> findWithPostByIds(@Param("ids") List<Long> ids);

    @Query(
            value = "SELECT r FROM Recruitment r INNER JOIN FETCH r.post INNER JOIN FETCH r.event e INNER JOIN FETCH e.post where r.id in :ids"
    )
    List<Recruitment> findWithPostAndEventAndEventPostByIds(@Param("ids") List<Long> ids);
}
