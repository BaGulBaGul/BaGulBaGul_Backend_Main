package com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.QueryPostWithConditionByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentConditionalRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryRecruitmentWithConditionByUserApplicationEvent extends QueryPostWithConditionByUserApplicationEvent {

    private RecruitmentConditionalRequest recruitmentConditionalRequest;

    public QueryRecruitmentWithConditionByUserApplicationEvent(RecruitmentConditionalRequest recruitmentConditionalRequest) {
        super(recruitmentConditionalRequest.getPostConditionalRequest());
        this.recruitmentConditionalRequest = recruitmentConditionalRequest;
    }
}
