package com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.listener;

import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.QueryPostDetailByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.QueryPostWithConditionByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.repository.PostRepository;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.repository.SearchKeywordRankingRepository;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.service.SearchKeywordRealtimeRankingService;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.service.TagRealtimeRankingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostStatisticsServiceImpl implements PostStatisticsService {

    private final PostRepository postRepository;
    private final TagRealtimeRankingService tagRealtimeRankingService;
    private final SearchKeywordRealtimeRankingService searchKeywordRealtimeRankingService;

    @Override
    @EventListener
    @Transactional
    public void handleQueryDetailByUser(QueryPostDetailByUserApplicationEvent event) {
        //조회수 증가
        postRepository.increaseViewsById(event.getPostDetailInfo().getPostId());
        //태그 랭킹 반영
        tagRealtimeRankingService.increaseTagScore1(event.getPostDetailInfo().getTags());
    }

    @Override
    @EventListener
    @Transactional
    public void handleQueryWithConditionByUser(QueryPostWithConditionByUserApplicationEvent event) {
        String title = event.getPostConditionalRequest().getTitle();
        if(title != null) {
            List<String> titles = List.of(title.split(" "));
            searchKeywordRealtimeRankingService.increaseKeywordScore1(titles);
        }
    }
}
