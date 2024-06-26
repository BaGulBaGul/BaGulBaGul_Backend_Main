package com.BaGulBaGul.BaGulBaGul.domain.post.repository;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentLike;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostCommentLikeRepository extends JpaRepository<PostCommentLike, Long> {
    @Modifying
    @Query(value =
            "DELETE FROM PostCommentLike pcl "
                + "WHERE pcl.postComment.id in ("
                    + "SELECT pc.id "
                    + "FROM PostComment pc "
                    + "WHERE pc.post = :post"
                + ")"
    )
    void deleteAllByPost(@Param("post") Post post);

    @Modifying
    @Query(value =
            "DELETE FROM PostCommentLike pcl WHERE pcl.postComment = :postComment"
    )
    void deleteAllByPostComment(@Param("postComment") PostComment postComment);

    boolean existsByPostCommentAndUser(PostComment postComment, User user);

    @Modifying
    @Query(value = "DELETE FROM PostCommentLike pcl WHERE pcl.postComment = :postComment and pcl.user = :user")
    int deleteAndGetCountByPostCommentAndUser(@Param("postComment") PostComment postComment, @Param("user") User user);
}
