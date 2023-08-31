package com.BaGulBaGul.BaGulBaGul.domain.post.repository;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChildLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostCommentChildLikeRepository extends JpaRepository<PostCommentChildLike, Long> {
    @Modifying
    @Query(value =
            "DELETE FROM PostCommentChildLike pchl "
                + "WHERE pchl.postCommentChild.id in ("
                    + "SELECT pch.id "
                    + "FROM PostCommentChild pch "
                    + "INNER JOIN pch.postComment pc "
                    + "WHERE pc.post = :post"
                + ")"
    )
    void deleteAllByPost(@Param("post") Post post);

    @Modifying
    @Query(value =
            "DELETE FROM PostCommentChildLike pchl "
                    + "WHERE pchl.postCommentChild.id in ("
                    + "SELECT pch.id "
                    + "FROM PostCommentChild pch "
                    + "WHERE pch.postComment = :postComment"
                    + ")"
    )
    void deleteAllByPostComment(@Param("postComment") PostComment postComment);

    @Modifying
    @Query(value =
            "DELETE FROM PostCommentChildLike pchl WHERE pchl.postCommentChild = :postCommentChild"
    )
    void deleteAllByPostCommentChild(@Param("postCommentChild") PostCommentChild postCommentChild);
}