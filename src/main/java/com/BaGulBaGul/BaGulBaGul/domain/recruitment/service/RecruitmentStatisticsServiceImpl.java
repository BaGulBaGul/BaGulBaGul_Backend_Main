package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostStatisticsService;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruitmentStatisticsServiceImpl implements RecruitmentStatisticsService {

    private final PostStatisticsService postStatisticsService;

    @Override
    @Async
    public void handleViewByUser(RecruitmentDetailResponse recruitmentDetailResponse) {
        postStatisticsService.handleViewByUser(recruitmentDetailResponse.getPost());
    }
}
