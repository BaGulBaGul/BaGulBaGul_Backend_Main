package com.BaGulBaGul.BaGulBaGul.domain.ranking.controller;

import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.api.request.TagRealtimeRankingApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.service.TagRealtimeRankingService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ranking/tags")
@RequiredArgsConstructor
@Api(tags = "실시간 랭킹")
public class TagRealtimeRankingControllerImpl implements TagRealtimeRankingController {

    private final TagRealtimeRankingService tagRealtimeRankingService;

    @Override
    @GetMapping("")
    public ApiResponse<List<String>> getTagRanking(@Valid TagRealtimeRankingApiRequest request) {
        return ApiResponse.of(
                tagRealtimeRankingService.getTagRanking(request.getCount())
        );
    }
}
