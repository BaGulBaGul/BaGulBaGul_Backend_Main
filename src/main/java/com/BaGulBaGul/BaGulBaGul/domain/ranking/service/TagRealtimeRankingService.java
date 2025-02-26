package com.BaGulBaGul.BaGulBaGul.domain.ranking.service;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.service.response.TagRankingItemInfo;
import java.util.Collection;
import java.util.List;

public interface TagRealtimeRankingService {
    //1위부터 count개수 만큼 조회
    List<String> getTagRanking(int count);
    //태그의 조회수 증가
    void increaseTagScore(Collection<TagRankingItemInfo> tags);
    //태그의 조회수 1 증가
    void increaseTagScore1(Collection<String> tags);
}
