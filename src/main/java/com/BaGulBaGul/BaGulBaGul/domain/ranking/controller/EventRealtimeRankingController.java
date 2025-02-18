package com.BaGulBaGul.BaGulBaGul.domain.ranking.controller;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.api.request.EventRealtimeRankingApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.api.response.EventRealtimeViewRankingApiResponse;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import java.util.List;

public interface EventRealtimeRankingController {
    ApiResponse<List<EventRealtimeViewRankingApiResponse>> getEventViewRanking(
            EventRealtimeRankingApiRequest eventRealtimeRankingApiRequest);
}
