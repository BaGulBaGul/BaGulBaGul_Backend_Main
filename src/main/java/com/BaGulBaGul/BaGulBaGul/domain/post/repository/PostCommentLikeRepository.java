package com.BaGulBaGul.BaGulBaGul.domain.post.repository;

import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentLikeRepository extends JpaRepository<PostCommentLike, PostCommentLike.PostCommentLikeId> {
}
