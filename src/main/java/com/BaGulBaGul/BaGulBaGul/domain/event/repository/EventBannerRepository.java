package com.BaGulBaGul.BaGulBaGul.domain.event.repository;

import com.BaGulBaGul.BaGulBaGul.domain.event.EventBanner;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventBannerRepository extends JpaRepository<EventBanner, Long> {

    @Query(value = "SELECT DISTINCT eb "
            + "FROM EventBanner eb "
                + "LEFT JOIN FETCH eb.bannerImageResource "
                + "LEFT JOIN FETCH eb.event e "
                + "LEFT JOIN FETCH e.categories ec "
                + "LEFT JOIN FETCH ec.category c "
                + "LEFT JOIN FETCH e.post p "
                + "LEFT JOIN FETCH p.user u "
                + "LEFT JOIN FETCH e.hostUser eh"
    )
    List<EventBanner> findAllWithResourceAndEventAndPostAndWriter();
}
