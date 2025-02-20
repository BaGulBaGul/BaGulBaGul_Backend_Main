package com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.listener;

import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.QueryPostDetailByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostStatisticsServiceImpl implements PostStatisticsService {

    private final PostRepository postRepository;

    @Override
    @EventListener
    @Transactional
    public void handleQueryDetailByUser(QueryPostDetailByUserApplicationEvent event) {
        postRepository.increaseViewsById(event.getPostDetailInfo().getPostId());
    }
}
