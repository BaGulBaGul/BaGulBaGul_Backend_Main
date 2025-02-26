package com.BaGulBaGul.BaGulBaGul.domain.ranking.controller;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.api.request.TagRealtimeRankingApiRequest;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import java.util.List;

public interface TagRealtimeRankingController {
    ApiResponse<List<String>> getTagRanking(TagRealtimeRankingApiRequest request);
}
