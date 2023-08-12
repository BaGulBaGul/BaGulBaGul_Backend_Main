package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Category;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;

public interface PostService {
    void clearCategory(Post post);
    void addCategory(Post post, String categoryName);
    void addCategory(Post post, Category category);
    void delete(Post post);
}
