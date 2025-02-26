package com.BaGulBaGul.BaGulBaGul.domain.event.applicationevent;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.QueryPostDetailByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.QueryPostWithConditionByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryEventWithConditionByUserApplicationEvent extends QueryPostWithConditionByUserApplicationEvent {

    private EventConditionalRequest eventConditionalRequest;

    public QueryEventWithConditionByUserApplicationEvent(EventConditionalRequest eventConditionalRequest) {
        super(eventConditionalRequest.getPostConditionalRequest());
        this.eventConditionalRequest = eventConditionalRequest;
    }
}
