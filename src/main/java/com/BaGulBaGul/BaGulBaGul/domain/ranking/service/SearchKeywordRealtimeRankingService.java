package com.BaGulBaGul.BaGulBaGul.domain.ranking.service;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.SearchKeywordRankingItemInfo;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.TagRankingItemInfo;
import java.util.Collection;
import java.util.List;

public interface SearchKeywordRealtimeRankingService {
    List<String> getKeywordRanking(int count);
    //검색어의 조회수 증가
    void increaseKeywordScore(Collection<SearchKeywordRankingItemInfo> keywords);
    //검색어의 조회수 1 증가
    void increaseKeywordScore1(Collection<String> keywords);
}
