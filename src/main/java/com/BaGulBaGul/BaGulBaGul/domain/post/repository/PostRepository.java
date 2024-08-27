package com.BaGulBaGul.BaGulBaGul.domain.post.repository;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT count(*) FROM Post p WHERE p.user.id = :userId")
    long countByUserId(@Param("userId") Long userId);

    @EntityGraph(attributePaths = {"user", "categories.category"})
    Optional<Post> findWithUserAndCategoriesById(Long id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update from Post post set post.likeCount = post.likeCount + 1 where post = :post")
    void increaseLikeCount(@Param(value = "post") Post post);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update from Post post set post.likeCount = post.likeCount - 1 where post = :post")
    void decreaseLikeCount(@Param(value = "post") Post post);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update from Post post set post.commentCount = post.commentCount + 1 where post = :post")
    void increaseCommentCount(@Param(value = "post") Post post);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update from Post post set post.commentCount = post.commentCount - 1 where post = :post")
    void decreaseCommentCount(@Param(value = "post") Post post);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update from Post post set post.views = post.views + 1 where post.id = :postId")
    void increaseViewsById(@Param(value = "postId") Long postId);
}
