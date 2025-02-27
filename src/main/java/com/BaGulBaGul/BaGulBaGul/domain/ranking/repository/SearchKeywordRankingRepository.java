package com.BaGulBaGul.BaGulBaGul.domain.ranking.repository;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.SearchKeywordRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.TagRankingItemInfo;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public interface SearchKeywordRankingRepository {
    void increase7DaysKeywords(Collection<? extends SearchKeywordRankingItemInfo> keywords);
    void decrease7DaysKeywords(Collection<? extends SearchKeywordRankingItemInfo> keywords);
    void deleteAll7DaysKeywords();
    List<String> getTopKKeywordsFrom7Days(int k);

    void increaseDayKeywords(LocalDateTime time, Collection<? extends SearchKeywordRankingItemInfo> keywords);
    void decreaseDayKeywords(LocalDateTime time, Collection<? extends SearchKeywordRankingItemInfo> keywords);
    Iterator<SearchKeywordRankingItemInfo> getDayKeywordsIterator(LocalDateTime time);
    void deleteAllDayKeywordsByTime(LocalDateTime time);
}
