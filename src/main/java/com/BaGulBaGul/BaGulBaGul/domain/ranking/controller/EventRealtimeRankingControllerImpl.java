package com.BaGulBaGul.BaGulBaGul.domain.ranking.controller;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.api.request.EventRealtimeRankingApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.dto.api.response.EventRealtimeViewRankingApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.ranking.service.EventRealtimeRankingService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ranking/event")
@RequiredArgsConstructor
@Api(tags = "실시간 랭킹")
public class EventRealtimeRankingControllerImpl implements EventRealtimeRankingController {

    private final EventRealtimeRankingService eventRealtimeRankingService;

    @Override
    @GetMapping("/views")
    public ApiResponse<List<EventRealtimeViewRankingApiResponse>> getEventViewRanking(
            EventRealtimeRankingApiRequest eventRankingRequest) {
        //조회수 랭킹 조회
        List<EventSimpleResponse> eventViewRanking = eventRealtimeRankingService.getEventViewRanking(
                eventRankingRequest.getEventType(),
                eventRankingRequest.getCount()
        );
        //api 응답으로 변환
        List<EventRealtimeViewRankingApiResponse> apiResponses = eventViewRanking.stream()
                .map(EventRealtimeViewRankingApiResponse::from)
                .collect(Collectors.toList());
        return ApiResponse.of(apiResponses);
    }
}
