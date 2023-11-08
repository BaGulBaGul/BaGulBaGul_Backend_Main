package com.BaGulBaGul.BaGulBaGul.domain.event.repository;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.querydsl.FindEventByCondition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long>, FindEventByCondition {
    @EntityGraph(attributePaths = {"post.user", "categories.category"})
    Optional<Event> findWithPostAndUserAndCategoryById(Long eventId);

    @Query(
            value = "SELECT e FROM Event e INNER JOIN e.post p INNER JOIN p.likes pl WHERE e.type = :type and pl.user.id = :userId"
    )
    Page<Event> getLikeEventByUserAndType(
            @Param("userId") Long userId, @Param("type")EventType type, Pageable pageable
    );

    @Query(
            value = "SELECT e FROM Event e INNER JOIN FETCH e.post where e.id in :ids"
    )
    List<Event> findWithPostByIds(List<Long> ids);
}
