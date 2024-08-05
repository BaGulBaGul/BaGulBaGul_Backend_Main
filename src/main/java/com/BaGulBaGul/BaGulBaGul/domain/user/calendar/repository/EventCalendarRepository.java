package com.BaGulBaGul.BaGulBaGul.domain.user.calendar.repository;

import com.BaGulBaGul.BaGulBaGul.domain.user.EventCalendar;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventCalendarRepository extends JpaRepository<EventCalendar, EventCalendar.EventCalendarId> {

    @Query("SELECT count(*) FROM EventCalendar ec WHERE ec.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @Query("SELECT ec FROM EventCalendar ec INNER JOIN FETCH ec.event e INNER JOIN FETCH e.post p "
            + "WHERE ec.user = :user AND "
            + "e.startDate <= :etime AND "
            + "e.endDate >= :stime")
    List<EventCalendar> findByCondition(
            @Param("user") User user,
            @Param("stime") LocalDateTime startTime,
            @Param("etime") LocalDateTime endTime
    );
}
