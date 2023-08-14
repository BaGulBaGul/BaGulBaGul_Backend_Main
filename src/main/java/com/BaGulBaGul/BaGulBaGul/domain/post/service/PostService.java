package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Category;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;

public interface PostService {
    void clearCategory(Post post);
    void addCategory(Post post, String categoryName);
    void addCategory(Post post, Category category);
    void addLike(Post post, User user) throws DuplicateLikeException;
    void deleteLike(Post post, User user) throws LikeNotExistException;
    boolean existsLike(Post post, User user);
    void delete(Post post);
}
