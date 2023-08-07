package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Category;
import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostCategory;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.PostSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.CategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ErrorCode;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PostCategoryRepository postCategoryRepository;

    @Transactional
    public PostDetailResponse getPostDetailById(Long postId) {
        Post post = postRepository.findWithUserAndCategoriesById(postId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        return PostDetailResponse.of(post);
    }

    @Override
    public Page<PostSimpleResponse> getPostPageByCondition(PostConditionalRequest postConditionalRequest, Pageable pageable) {
        return postRepository.findPostSimpleResponsePageByCondition(postConditionalRequest, pageable);
    }

    @Override
    @Transactional
    public Long registerPost(Long userId, PostRegisterRequest postRegisterRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
        //post 생성
        Post post = Post.builder()
                .type(postRegisterRequest.getType())
                .user(user)
                .title(postRegisterRequest.getTitle())
                .content(postRegisterRequest.getContent())
                .headCount(postRegisterRequest.getHeadCount())
                .startDate(postRegisterRequest.getStartDate())
                .endDate(postRegisterRequest.getEndDate())
                .tags(postRegisterRequest.getTags().stream().collect(Collectors.joining(" ")))
                .image_url(postRegisterRequest.getImage_url())
                .likeCount(0)
                .commentCount(0)
                .build();
        postRepository.save(post);
        //카테고리 등록
        for(String categoryName : postRegisterRequest.getCategories()) {
            //존재하지 않는다면 잘못된 요청
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new GeneralException(ErrorCode.BAD_REQUEST));
            postCategoryRepository.save(
                    new PostCategory(post, category)
            );
        }
        return post.getId();
    }
}
