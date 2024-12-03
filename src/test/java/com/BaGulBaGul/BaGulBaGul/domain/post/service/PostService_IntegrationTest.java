package com.BaGulBaGul.BaGulBaGul.domain.post.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.api.request.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.sampledata.PostSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import java.util.Collections;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test2")
class PostService_IntegrationTest {

    @MockBean
    PostImageService postImageService;

    @Autowired
    PostService postService;

    @Autowired
    UserJoinService userJoinService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    EntityManager entityManager;


    @Nested
    @DisplayName("게시글 등록 테스트")
    class PostRegisterTest {
        @BeforeEach
        void init() {
            doNothing().when(postImageService).setImages(any(), any());
        }

        @Test
        @DisplayName("성공")
        @Transactional
        void shouldOK() {
            //given
            User user = userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST);
            PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
                    .title(PostSample.NORMAL_TITLE)
                    .content(PostSample.NORMAL_CONTENT)
                    .tags(PostSample.NORMAL_TAGS)
                    .imageIds(Collections.emptyList())
                    .build();

            //when
            Post post = postService.registerPost(user, postRegisterRequest);

            //then
            assertThat(post.getTitle()).isEqualTo(postRegisterRequest.getTitle());
            assertThat(post.getContent()).isEqualTo(postRegisterRequest.getContent());
            assertThat(post.getTags().split(" ")).contains(postRegisterRequest.getTags().toArray(new String[0]));
            verify(postImageService, times(1)).setImages(any(), any());
        }
    }

    @Nested
    @DisplayName("게시글 수정 테스트")
    class PostModifyTest {
        @BeforeEach
        void init() {
            doNothing().when(postImageService).setImages(any(), any());
        }

        @Test
        @DisplayName("성공")
        @Transactional
        void shouldOK() {
            //given
            User user = userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST);
            PostRegisterRequest postRegisterRequest = PostRegisterRequest.builder()
                    .title(PostSample.NORMAL_TITLE)
                    .content(PostSample.NORMAL_CONTENT)
                    .tags(PostSample.NORMAL_TAGS)
                    .imageIds(Collections.emptyList())
                    .build();
            PostModifyRequest postModifyRequest = PostModifyRequest.builder()
                    .title(PostSample.NORMAL2_TITLE)
                    .content(PostSample.NORMAL2_CONTENT)
                    .tags(PostSample.NORMAL2_TAGS)
                    .imageIds(Collections.emptyList())
                    .build();
            Post post = postService.registerPost(user, postRegisterRequest);

            //when
            postService.modifyPost(post, postModifyRequest);
            entityManager.flush();
            entityManager.clear();
            post = postRepository.findById(post.getId()).orElse(null);

            //then
            assertThat(post.getTitle()).isEqualTo(postModifyRequest.getTitle());
            assertThat(post.getContent()).isEqualTo(postModifyRequest.getContent());
            assertThat(post.getTags().split(" ")).contains(postModifyRequest.getTags().toArray(new String[0]));
            verify(postImageService, times(2)).setImages(any(), any());
        }
    }
}

