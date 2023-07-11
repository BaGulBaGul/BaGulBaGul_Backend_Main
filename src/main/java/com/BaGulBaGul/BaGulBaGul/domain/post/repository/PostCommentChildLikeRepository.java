package com.BaGulBaGul.BaGulBaGul.domain.post.repository;

import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChildLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostCommentChildLikeRepository extends JpaRepository<PostCommentChildLike, PostCommentChildLike.PostCommentChildLikeId> {
}
