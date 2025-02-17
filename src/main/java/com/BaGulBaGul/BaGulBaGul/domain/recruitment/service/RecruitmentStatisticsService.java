package com.BaGulBaGul.BaGulBaGul.domain.recruitment.service;

import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentDetailResponse;

public interface RecruitmentStatisticsService {
    void handleViewByUser(RecruitmentDetailResponse recruitmentDetailResponse);
}
