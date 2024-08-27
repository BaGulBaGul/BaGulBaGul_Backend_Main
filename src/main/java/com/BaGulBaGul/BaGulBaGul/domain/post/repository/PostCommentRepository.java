package com.BaGulBaGul.BaGulBaGul.domain.post.repository;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentPageResponse;
import java.util.List;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostCommentDetailResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostCommentRepository extends JpaRepository<PostComment, Long> {
    @Query(
            value = "SELECT new com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.PostCommentDetailResponse( "
                        + "pc.id, "
                        + "user.id, "
                        + "user.nickname, "
                        + "user.imageURI, "
                        + "pc.content, "
                        + "pc.commentChildCount, "
                        + "pc.likeCount, "
                        + "pc.createdAt"
                    + ") "
                    + "FROM PostComment pc "
                        + "LEFT OUTER JOIN pc.user user "
                    + "WHERE pc.id = :postCommentId"
    )
    PostCommentDetailResponse getPostCommentDetail(@Param("postCommentId") Long postCommentId);

    @Modifying
    @Query(value = "DELETE FROM PostComment pc WHERE pc.post = :post")
    void deleteAllByPost(@Param("post") Post post);

    @Query(
            value = "SELECT new com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentPageResponse( "
                    + "pc.id, "
                    + "user.id, "
                    + "user.nickname, "
                    + "user.imageURI, "
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
                + "LEFT OUTER JOIN pc.user user "
                + "LEFT OUTER JOIN pc.likes pcl ON pcl.user.id = :requestUserId "
            + "WHERE pc.post.id = :postId"
    )
    List<GetPostCommentPageResponse> getPostCommentPageWithMyLike(
            @Param("postId") Long postId,
            @Param("requestUserId") Long requestUserId,
            Pageable pageable
    );

    @Query(
            value = "SELECT new com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.response.GetPostCommentPageResponse( "
                    + "pc.id, "
                    + "user.id, "
                    + "user.nickname, "
                    + "user.imageURI, "
                    + "pc.content, "
                    + "pc.commentChildCount, "
                    + "pc.likeCount, "
                    + "false, "
                    + "pc.createdAt"
                    + ") "
                    + "FROM PostComment pc "
                    + "LEFT OUTER JOIN pc.user user "
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

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update from PostComment pc set pc.commentChildCount = pc.commentChildCount + 1 where pc = :postComment")
    void increaseCommentChildCount(@Param(value = "postComment") PostComment postComment);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update from PostComment pc set pc.commentChildCount = pc.commentChildCount - 1 where pc = :postComment")
    void decreaseCommentChildCount(@Param(value = "postComment") PostComment postComment);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update from PostComment pc set pc.likeCount = pc.likeCount + 1 where pc = :postComment")
    void increaseLikeCount(@Param(value = "postComment") PostComment postComment);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "update from PostComment pc set pc.likeCount = pc.likeCount - 1 where pc = :postComment")
    void decreaseLikeCount(@Param(value = "postComment") PostComment postComment);
}
