package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.GetPostCommentPageResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostCommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostCommentAPIServiceImpl implements PostCommentAPIService {

    private final PostCommentRepository postCommentRepository;

    @Override
    public Page<GetPostCommentPageResponse> getPostCommentPage(Long postId, Long requestUserId, Pageable pageable) {
        //repo에서 Page를 바로 받아오면 count query에서는 requestUserId를 쓰지 않아서 Could not locate named parameter예외 발생
        List<GetPostCommentPageResponse> result;
        //requestUserId가 null이라면 left outer join이 필요 없음
        if(requestUserId == null) {
            result = postCommentRepository.getPostCommentPage(postId, pageable);
        }
        else {
            result = postCommentRepository.getPostCommentPageWithMyLike(postId, requestUserId, pageable);
        }
        Long count = postCommentRepository.getPostCommentPageWithMyLikeCount(postId);
        return new PageImpl(result, pageable, count);
    }
}
