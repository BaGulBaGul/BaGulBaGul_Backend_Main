package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;

public interface PostStatisticsService {
    void handleViewByUser(PostDetailInfo postDetailInfo);
}
