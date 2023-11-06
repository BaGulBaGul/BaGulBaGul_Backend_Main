package com.BaGulBaGul.BaGulBaGul.domain.event.repository;

import com.BaGulBaGul.BaGulBaGul.domain.event.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
