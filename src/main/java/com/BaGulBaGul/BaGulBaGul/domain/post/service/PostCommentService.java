package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostComment;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;

public interface PostCommentService {
    PostComment registerComment(Post post, User user, String content);
    void deleteComment(PostComment postComment);
    PostCommentChild registerCommentChild(PostComment postComment, User user, String content);
    void deleteCommentChild(PostCommentChild postCommentChild);
    void addLikeToComment(PostComment postComment, User user) throws DuplicateLikeException;
    void deleteLikeToComment(PostComment postComment, User user) throws LikeNotExistException;
}
