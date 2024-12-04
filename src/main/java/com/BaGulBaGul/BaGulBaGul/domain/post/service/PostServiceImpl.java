package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.Post;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostImage;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostLike;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostSimpleInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostWriterInfo;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.DuplicateLikeException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.LikeNotExistException;
import com.BaGulBaGul.BaGulBaGul.domain.post.exception.PostNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentChildRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostLikeRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.global.exception.NoPermissionException;
import com.BaGulBaGul.BaGulBaGul.global.upload.service.ResourceService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostCommentLikeRepository postCommentLikeRepository;
    private final PostCommentChildRepository postCommentChildRepository;
    private final PostCommentChildLikeRepository postCommentChildLikeRepository;
    private final PostImageService postImageService;
    private final ResourceService resourceService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional
    public PostSimpleInfo getPostSimpleInfo(Long postId) {
        //Post정보를 가져옴
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException());

        //작성자 정보를 생성
        PostWriterInfo writerInfo = PostWriterInfo.of(post.getUser());

        //태그를 리스트로 변환
        List<String> tags = splitTags(post.getTags());

        //최종 PostSimpleInfo를 생성 후 반환
        return PostSimpleInfo.builder()
                .postId(post.getId())
                .writer(writerInfo)
                .title(post.getTitle())
                .headImageUrl(post.getImage_url())
                .tags(tags)
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();
    }



    @Override
    @Transactional
    public PostDetailInfo getPostDetailInfo(Long postId) {
        //Post정보를 가져옴
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException());

        //작성자 정보를 생성
        PostWriterInfo writerInfo = PostWriterInfo.of(post.getUser());

        //태그를 리스트로 변환
        List<String> tags = splitTags(post.getTags());

        //연결된 이미지의 resource id와 url을 조회
        List<PostImage> images = postImageService.getByOrder(post);
        List<Long> resourceIds = images.stream().map(x -> x.getResource().getId()).collect(Collectors.toList());
        List<String> imageUrls = resourceService.getResourceUrlsFromIds(resourceIds);

        //PostDetailInfo를 생성해서 반환
        return PostDetailInfo.builder()
                .postId(post.getId())
                .writer(writerInfo)
                .title(post.getTitle())
                .headImageUrl(post.getImage_url())
                .content(post.getContent())
                .tags(tags)
                .imageIds(resourceIds)
                .imageUrls(imageUrls)
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .views(post.getViews())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();
    }

    @Override
    @Transactional
    public Post registerPost(User user, PostRegisterRequest postRegisterRequest) {
        Post post = Post.builder()
                .user(user)
                .title(postRegisterRequest.getTitle())
                .content(postRegisterRequest.getContent())
                .tags(postRegisterRequest.getTags().stream().collect(Collectors.joining(" ")))
                .likeCount(0)
                .commentCount(0)
                .views(0)
                .build();
        //post 생성
        postRepository.save(post);
        //이미지 연결
        postImageService.setImages(post, postRegisterRequest.getImageIds());
        return post;
    }

    @Override
    @Transactional
    public void modifyPost(Post post, PostModifyRequest postModifyRequest) {
        //patch 방식으로 postModifyRequest에서 null이 아닌 모든 필드를 변경
        if(postModifyRequest.getTitle() != null && !postModifyRequest.getTitle().isEmpty()) {
            post.setTitle(postModifyRequest.getTitle());
        }
        if(postModifyRequest.getContent() != null) {
            post.setContent(postModifyRequest.getContent());
        }
        if(postModifyRequest.getTags() != null) {
            post.setTags(postModifyRequest.getTags().stream().collect(Collectors.joining(" ")));
        }
        //이미지 연결 수정
        if(postModifyRequest.getImageIds() != null) {
            postImageService.setImages(post, postModifyRequest.getImageIds());
        }
    }

    @Override
    @Transactional
    public void deletePost(Post post) {
        postLikeRepository.deleteAllByPost(post);
        postCommentChildLikeRepository.deleteAllByPost(post);
        postCommentChildRepository.deleteAllByPost(post);
        postCommentLikeRepository.deleteAllByPost(post);
        postCommentRepository.deleteAllByPost(post);
        postImageService.setImages(post, null);
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public void addLike(Post post, User user) throws DuplicateLikeException {
        //PostLike에 insert하는 것은 FK가 참조하는 Post에 share lock을 거는데 likeCount의 update는 Post에 write lock을 거므로
        //먼저 likeCount를 증가시키지 않으면 deadlock이 일어날 수 있음.
        //아니면 FK 제약조건을 해제하는 것도 고려
        postRepository.increaseLikeCount(post);
        try{
            postLikeRepository.save(new PostLike(post, user));
            postLikeRepository.flush();
        }
        catch (DataIntegrityViolationException dataIntegrityViolationException) {
            //unique 제약조건 실패 = 이미 좋아요 존재 따라서 rollback
            throw new DuplicateLikeException();
        }
    }

    @Override
    @Transactional
    public void deleteLike(Post post, User user) throws LikeNotExistException {
        //add와 마찬가지 이유로 먼저 write lock을 획득
        postRepository.decreaseLikeCount(post);
        //요청한 PostLike를 지움
        int deletedCount = postLikeRepository.deleteAndGetCountByPostAndUser(post, user);
        //지운 행의 개수가 0 = 좋아요가 없었다 따라서 예외 던지고 rollback
        if(deletedCount == 0) {
            throw new LikeNotExistException();
        }
    }

    @Override
    public boolean existsLike(Post post, User user) {
        return postLikeRepository.existsByPostAndUser(post, user);
    }

    @Override
    public void checkWritePermission(Post post, User user) throws NoPermissionException {
        //작성자인지 확인
        if(user.getId().equals(post.getUser().getId())) {
            return;
        }
        throw new NoPermissionException();
    }

    private List<String> splitTags(String tags) {
        List<String> splittedTags = tags.equals("") ?
                new ArrayList<>() :
                Arrays.asList(tags.split(" ")).stream().filter(s -> !s.isEmpty()).collect(Collectors.toList());
        return splittedTags;
    }
}
