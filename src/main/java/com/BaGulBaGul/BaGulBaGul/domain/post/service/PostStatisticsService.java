package com.BaGulBaGul.BaGulBaGul.domain.post.service;

import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.QueryPostDetailByUserApplicationEvent;

public interface PostStatisticsService {

    //유저가 게시글을 상세조회 했을 때의 처리
    void handleQueryDetailByUser(QueryPostDetailByUserApplicationEvent event);
}
