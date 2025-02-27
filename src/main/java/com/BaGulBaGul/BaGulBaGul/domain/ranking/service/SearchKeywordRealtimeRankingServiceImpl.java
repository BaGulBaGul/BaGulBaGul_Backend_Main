package com.BaGulBaGul.BaGulBaGul.domain.ranking.service;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.SearchKeywordRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.TagRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.repository.SearchKeywordRankingRepository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchKeywordRealtimeRankingServiceImpl implements SearchKeywordRealtimeRankingService {

    private final SearchKeywordRankingRepository searchKeywordRankingRepository;

    @Override
    public List<String> getKeywordRanking(int count) {
        return searchKeywordRankingRepository.getTopKKeywordsFrom7Days(count);
    }

    @Override
    public void increaseKeywordScore(Collection<SearchKeywordRankingItemInfo> keywords) {
        searchKeywordRankingRepository.increaseDayKeywords(LocalDateTime.now(), keywords);
        searchKeywordRankingRepository.increase7DaysKeywords(keywords);
    }

    @Override
    public void increaseKeywordScore1(Collection<String> keywords) {
        increaseKeywordScore(
                keywords.stream()
                        .map(keyword -> new SearchKeywordRankingItemInfo(keyword, 1L))
                        .collect(Collectors.toList())
        );
    }
}
