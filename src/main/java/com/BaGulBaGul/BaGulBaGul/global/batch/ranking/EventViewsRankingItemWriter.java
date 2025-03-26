package com.BaGulBaGul.BaGulBaGul.global.batch.ranking;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.EventViewsRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.repository.EventViewRankingRepository;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

@Builder
@RequiredArgsConstructor
public class EventViewsRankingItemWriter implements ItemWriter<EventViewsRankingItemInfo> {

    private final EventViewRankingRepository eventViewRankingRepository;
    private final EventType eventType;

    @Override
    public void write(List<? extends EventViewsRankingItemInfo> items) throws Exception {
        eventViewRankingRepository.decrease7DaysViewCounts(eventType, items);
    }
}
