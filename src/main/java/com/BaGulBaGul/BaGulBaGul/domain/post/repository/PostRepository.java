package com.BaGulBaGul.BaGulBaGul.domain.post.repository;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import java.util.Optional;

import com.BaGulBaGul.BaGulBaGul.domain.post.repository.queryDSL.FindPostByCondition;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, FindPostByCondition {
    @EntityGraph(attributePaths = {"user", "categories.category"})
    Optional<Post> findWithUserAndCategoriesById(Long id);
}
