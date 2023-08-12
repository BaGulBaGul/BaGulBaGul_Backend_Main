package com.BaGulBaGul.BaGulBaGul.domain.post.repository;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {//PostCategory.PostCategoryId> {
    @Modifying
    @Query(value = "DELETE FROM PostCategory pc WHERE pc.post = :post")
    void deleteAllByPost(@Param("post") Post post);
}
