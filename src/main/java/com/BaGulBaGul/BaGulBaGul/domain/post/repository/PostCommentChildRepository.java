package com.BaGulBaGul.BaGulBaGul.domain.post.repository;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentChildPageResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostCommentChildRepository extends JpaRepository<PostCommentChild, Long> {
    @Modifying
    @Query(value =
            "DELETE FROM PostCommentChild pch "
                + "WHERE pch.postComment.id in ("
                    + "SELECT pc.id "
                    + "FROM PostComment pc "
                    + "WHERE pc.post = :post"
                + ")"
    )
    void deleteAllByPost(@Param("post") Post post);

    @Modifying
    @Query(value =
            "DELETE FROM PostCommentChild pch WHERE pch.postComment = :postComment"
    )
    void deleteAllByPostComment(@Param("postComment") PostComment postComment);

    @Query(
            value = "SELECT new com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentChildPageResponse( "
                        + "pch.id, "
                        + "user.id, "
                        + "user.nickname, "
                        + "user.imageURI, "
                        + "rp_user.nickname, "
                        + "pch.content, "
                        + "pch.likeCount, "
                        + "CASE "
                            + "WHEN pchl.id IS NULL THEN false "
                            + "ELSE true "
                        + "END, "
                        + "pch.createdAt"
                    + ") "
                    + "FROM PostCommentChild pch "
                        + "LEFT OUTER JOIN pch.user user "
                        + "LEFT OUTER JOIN pch.replyTargetUser rp_user "
                        + "LEFT OUTER JOIN pch.likes pchl ON pchl.user.id = :requestUserId "
                    + "WHERE pch.postComment.id = :postCommentId"
    )
    List<GetPostCommentChildPageResponse> getPostCommentChildPageWithMyLike(
            @Param("postCommentId") Long postCommentId,
            @Param("requestUserId") Long requestUserId,
            Pageable pageable
    );

    @Query(
            value = "SELECT new com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentChildPageResponse( "
                    + "pch.id, "
                    + "user.id, "
                    + "user.nickname, "
                    + "user.imageURI, "
                    + "rp_user.nickname, "
                    + "pch.content, "
                    + "pch.likeCount, "
                    + "false, "
                    + "pch.createdAt"
                    + ") "
                    + "FROM PostCommentChild pch "
                    + "LEFT OUTER JOIN pch.user user "
                    + "LEFT OUTER JOIN pch.replyTargetUser rp_user "
                    + "WHERE pch.postComment.id = :postCommentId"
    )
    List<GetPostCommentChildPageResponse> getPostCommentChildPage(
            @Param("postCommentId") Long postCommentId,
            Pageable pageable
    );

    @Query("SELECT count(*) FROM PostCommentChild pch WHERE pch.postComment.id = :postCommentId")
    Long getPostCommentChildPageCount (
            @Param("postCommentId") Long postCommentId
    );

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE FROM PostCommentChild pch SET pch.likeCount = pch.likeCount + 1 WHERE pch = :postCommentChild")
    void increaseLikeCount(@Param(value = "postCommentChild") PostCommentChild postCommentChild);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE FROM PostCommentChild pch SET pch.likeCount = pch.likeCount - 1 WHERE pch = :postCommentChild")
    void decreaseLikeCount(@Param(value = "postCommentChild") PostCommentChild postCommentChild);
}
