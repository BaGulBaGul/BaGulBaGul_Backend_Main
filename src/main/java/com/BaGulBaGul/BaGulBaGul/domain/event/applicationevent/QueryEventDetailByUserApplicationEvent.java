package com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.QueryPostDetailByUserApplicationEvent;
import lombok.Getter;
import lombok.Setter;

/*
 * 유저가 특정 이벤트를 상세조회 했을 때 발생하는 어플리케이션 이벤트
 */
@Getter
@Setter
public class QueryEventDetailByUserApplicationEvent extends QueryPostDetailByUserApplicationEvent {
    private EventDetailResponse eventDetailResponse;
    public QueryEventDetailByUserApplicationEvent(EventDetailResponse eventDetailResponse) {
        super(eventDetailResponse.getPost());
        this.eventDetailResponse = eventDetailResponse;
    }
}
