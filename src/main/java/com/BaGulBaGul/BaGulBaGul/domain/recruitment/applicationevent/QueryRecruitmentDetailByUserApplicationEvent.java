package com.BaGulBaGul.BaGulBaGul.domain.recruitment.applicationevent;

import com.BaGulBaGul.BaGulBaGul.domain.post.applicationevent.QueryPostDetailByUserApplicationEvent;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.response.RecruitmentDetailResponse;
import lombok.Getter;
import lombok.Setter;

/*
 * 유저가 특정 모집글을 상세조회 했을 때 발생하는 어플리케이션 이벤트
 */
@Getter
@Setter
public class QueryRecruitmentDetailByUserApplicationEvent extends QueryPostDetailByUserApplicationEvent {

    private RecruitmentDetailResponse recruitmentDetailResponse;

    public QueryRecruitmentDetailByUserApplicationEvent(RecruitmentDetailResponse recruitmentDetailResponse) {
        super(recruitmentDetailResponse.getPost());
        this.recruitmentDetailResponse = recruitmentDetailResponse;
    }
}
