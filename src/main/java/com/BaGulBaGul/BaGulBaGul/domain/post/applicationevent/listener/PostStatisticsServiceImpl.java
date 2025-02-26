package com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.listener;

import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.QueryPostDetailByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.service.TagRealtimeRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostStatisticsServiceImpl implements PostStatisticsService {

    private final PostRepository postRepository;
    private final TagRealtimeRankingService tagRealtimeRankingService;

    @Override
    @EventListener
    @Transactional
    public void handleQueryDetailByUser(QueryPostDetailByUserApplicationEvent event) {
        //조회수 증가
        postRepository.increaseViewsById(event.getPostDetailInfo().getPostId());
        //태그 랭킹 반영
        tagRealtimeRankingService.increaseTagScore1(event.getPostDetailInfo().getTags());
    }
}
