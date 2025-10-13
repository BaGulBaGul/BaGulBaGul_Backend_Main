package com.BaGulBaGul.BaGulBaGul.domain.event.repository;

import com.BaGulBaGul.BaGulBaGul.domain.event.EventBanner;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventBannerRepository extends JpaRepository<EventBanner, Long> {
}
