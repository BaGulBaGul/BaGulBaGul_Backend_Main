package com.BaGulBaGul.BaGulBaGul.domain.event.repository;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.querydsl.FindEventByCondition;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
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

    //카테고리와 같은 1:N:1 관계는 반드시 left outer join 으로 fetch join 해 줘야 한다. 카테고리가 없는 엔티티도 fetch join하기 위해.
    //user과 같이 삭제되어 null이 될수 있는 관계도 left outer join으로 fetch join이 필요
    @Query(
            value = "SELECT e FROM Event e LEFT JOIN FETCH e.categories ec LEFT JOIN FETCH ec.category LEFT JOIN FETCH e.hostUser eh INNER JOIN FETCH e.post p LEFT JOIN FETCH p.user WHERE e.id in :eventIds"
    )
    List<Event> findWithPostAndUserAndCategoriesByIds(@Param("eventIds") List<Long> eventIds);

    @Query(
            value = "SELECT e FROM Event e INNER JOIN e.post p INNER JOIN p.likes pl WHERE e.type = :type and pl.user.id = :userId"
    )
    Page<Event> getLikeEventByUserAndType(
            @Param("userId") Long userId, @Param("type")EventType type, Pageable pageable
    );

    @Query(
            value = "SELECT e FROM Event e INNER JOIN FETCH e.post where e.id in :ids"
    )
    List<Event> findWithPostByIds(@Param("ids") List<Long> ids);

    @Query(
            value = "SELECT e FROM Event e INNER JOIN FETCH e.post p INNER JOIN FETCH p.user u WHERE e.id in :ids"
    )
    List<Event> findWithPostAndUserByIds(@Param("ids") List<Long> ids);

    Event findByPost(Post post);

    List<Event> findByHostUser(User eventHostUser);
}
