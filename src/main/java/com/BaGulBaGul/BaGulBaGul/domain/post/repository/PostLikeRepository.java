package com.BaGulBaGul.BaGulBaGul.domain.post.repository;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostLike;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    @Modifying
    @Query(value = "DELETE FROM PostLike pl WHERE pl.post = :post")
    void deleteAllByPost(@Param("post") Post post);

    @Modifying
    @Query(value = "DELETE FROM PostLike pl WHERE pl.post = :post and pl.user = :user")
    int deleteAndGetCountByPostAndUser(@Param("post") Post post, @Param("user") User user);

    boolean existsByPostAndUser(Post post, User user);
}
