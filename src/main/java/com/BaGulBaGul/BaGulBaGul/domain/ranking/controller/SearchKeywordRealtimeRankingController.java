package com.BaGulBaGul.BaGulBaGul.domain.ranking.controller;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.api.request.SearchKeywordRealtimeRankingApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.api.request.TagRealtimeRankingApiRequest;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import java.util.List;

public interface SearchKeywordRealtimeRankingController {
    ApiResponse<List<String>> getKeywordRanking(SearchKeywordRealtimeRankingApiRequest request);
}
