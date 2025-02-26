package com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.listener;

import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.QueryPostDetailByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.QueryPostWithConditionByUserApplicationEvent;

public interface PostStatisticsService {

    //유저가 게시글을 상세조회 했을 때의 처리
    void handleQueryDetailByUser(QueryPostDetailByUserApplicationEvent event);
    //유저가 게시글을 조건 검색했을 때의 처리
    void handleQueryWithConditionByUser(QueryPostWithConditionByUserApplicationEvent event);
}
