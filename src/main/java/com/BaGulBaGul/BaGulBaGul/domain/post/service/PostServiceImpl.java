package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Category;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCategory;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.CategoryNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.CategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCommentLikeRepository postCommentLikeRepository;
    private final PostCommentChildRepository postCommentChildRepository;
    private final PostCommentChildLikeRepository postCommentChildLikeRepository;

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

    @Override
    @Transactional
    public void delete(Post post) {
        postCategoryRepository.deleteAllByPost(post);
        postLikeRepository.deleteAllByPost(post);
        postCommentChildLikeRepository.deleteAllByPost(post);
        postCommentChildRepository.deleteAllByPost(post);
        postCommentLikeRepository.deleteAllByPost(post);
        postCommentRepository.deleteAllByPost(post);
        postRepository.delete(post);
    }
}
