package com.BaGulBaGul.BaGulBaGul.domain.ranking.controller;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.api.request.SearchKeywordRealtimeRankingApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.service.SearchKeywordRealtimeRankingService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ranking/search")
@RequiredArgsConstructor
@Api(tags = "실시간 랭킹")
public class SearchKeywordRealtimeRankingControllerImpl implements SearchKeywordRealtimeRankingController {

    private final SearchKeywordRealtimeRankingService searchKeywordRealtimeRankingService;

    @Override
    @GetMapping("")
    public ApiResponse<List<String>> getKeywordRanking(SearchKeywordRealtimeRankingApiRequest request) {
        return ApiResponse.of(
                searchKeywordRealtimeRankingService.getKeywordRanking(request.getCount())
        );
    }
}
