package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Category;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCategory;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCommentChild;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.CategoryNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.CategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostLikeRepository;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostCategoryRepository postCategoryRepository;
    private final CategoryRepository categoryRepository;
    @Override
    @Transactional
    public void clearCategory(Post post) {
        postCategoryRepository.deleteAll(post.getCategories());
        post.getCategories().clear();
        postCategoryRepository.flush();
    }

    @Override
    @Transactional
    public void addCategory(Post post, String categoryName) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow(CategoryNotFoundException::new);
        this.addCategory(post, category);
    }

    @Override
    @Transactional
    public void addCategory(Post post, Category category) {
        PostCategory postCategory = new PostCategory(post, category);
        post.getCategories().add(postCategory);
    }

}
