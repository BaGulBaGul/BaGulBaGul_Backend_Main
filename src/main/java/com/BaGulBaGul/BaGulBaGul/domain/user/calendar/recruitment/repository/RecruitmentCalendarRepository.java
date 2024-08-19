package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.recruitment.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.RecruitmentCalendar;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RecruitmentCalendarRepository extends JpaRepository<RecruitmentCalendar, RecruitmentCalendar.RecruitmentCalendarId> {
    @Query("SELECT rc FROM RecruitmentCalendar rc INNER JOIN FETCH rc.recruitment r INNER JOIN FETCH r.post p "
            + "WHERE rc.user = :user AND "
            + "r.startDate <= :etime AND "
            + "r.endDate >= :stime")
    List<RecruitmentCalendar> findWithRecruitmentAndPostByCondition(
            @Param("user") User user,
            @Param("stime") LocalDateTime startTime,
            @Param("etime") LocalDateTime endTime
    );
}
