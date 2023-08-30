package com.BaGulBaGul.BaGulBaGul.domain.post.repository;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentPageResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    @Modifying
    @Query(value = "DELETE FROM PostComment pc WHERE pc.post = :post")
    void deleteAllByPost(@Param("post") Post post);

    @Query(
            value = "SELECT new com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentPageResponse( "
                    + "pc.id, "
                    + "user.id, "
                    + "user.nickname, "
                    + "pc.content, "
                    + "pc.commentChildCount, "
                    + "pc.likeCount, "
                    + "CASE "
                        + "WHEN pcl.id IS NULL THEN false "
                        + "ELSE true "
                    + "END, "
                    + "pc.createdAt"
                    + ") "
            + "FROM PostComment pc "
                + "INNER JOIN pc.user user "
                + "LEFT OUTER JOIN pc.likes pcl ON pcl.user.id = :requestUserId "
            + "WHERE pc.post.id = :postId"
    )
    List<GetPostCommentPageResponse> getPostCommentPageWithMyLike(
            @Param("postId") Long postId,
            @Param("requestUserId") Long requestUserId,
            Pageable pageable
    );

    @Query(
            value = "SELECT new com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentPageResponse( "
                    + "pc.id, "
                    + "user.id, "
                    + "user.nickname, "
                    + "pc.content, "
                    + "pc.commentChildCount, "
                    + "pc.likeCount, "
                    + "false, "
                    + "pc.createdAt"
                    + ") "
                    + "FROM PostComment pc "
                    + "INNER JOIN pc.user user "
                    + "WHERE pc.post.id = :postId"
    )
    List<GetPostCommentPageResponse> getPostCommentPage(
            @Param("postId") Long postId,
            Pageable pageable
    );

    @Query("SELECT count(*) FROM PostComment pc WHERE pc.post.id = :postId")
    Long getPostCommentPageWithMyLikeCount (
            @Param("postId") Long postId
    );

    @Modifying(clearAutomatically = true)
    @Query(value = "update from PostComment pc set pc.commentChildCount = pc.commentChildCount + 1 where pc = :postComment")
    void increaseCommentChildCount(@Param(value = "postComment") PostComment postComment);

    @Modifying(clearAutomatically = true)
    @Query(value = "update from PostComment pc set pc.commentChildCount = pc.commentChildCount - 1 where pc = :postComment")
    void decreaseCommentChildCount(@Param(value = "postComment") PostComment postComment);
}
