package com.BaGulBaGul.BaGulBaGul.global.batch.ranking;

import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.EventViewsRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.repository.EventViewRankingRepository;
import java.time.LocalDateTime;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
public class EventViewsRankingItemReader implements ItemReader<EventViewsRankingItemInfo> {

    private final Iterator<EventViewsRankingItemInfo> iterator;

    public EventViewsRankingItemReader(
            EventViewRankingRepository eventViewRankingRepository,
            EventType eventType,
            LocalDateTime time
    ) {
        this.iterator = eventViewRankingRepository.getDayViewCountIterator(
                eventType,
                time
        );
    }

    @Override
    public EventViewsRankingItemInfo read()
            throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException
    {
        while (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }
}
