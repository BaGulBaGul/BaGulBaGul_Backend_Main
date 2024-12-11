package com.BaGulBaGul.BaGulBaGul.domain.recruitment.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.sampledata.ParticipantStatusSample;
import com.BaGulBaGul.BaGulBaGul.domain.common.sampledata.PeriodSample;
import com.BaGulBaGul.BaGulBaGul.domain.post.sampledata.PostSample;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.Recruitment;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentRegisterRequest;
import java.time.LocalDateTime;
import java.time.Month;

public class RecruitmentSample {
    public static final RecruitmentState NORMAL_RECRUITMENT_STATE = RecruitmentState.PROCEEDING;
    public static final Boolean NORMAL_DELETED = false;

    public static Recruitment getNormal() {
        RecruitmentRegisterRequest recruitmentRegisterRequest = getNormalRegisterRequest();
        Recruitment recruitment = Recruitment.builder()
                .currentHeadCount(recruitmentRegisterRequest.getParticipantStatusRegisterRequest().getCurrentHeadCount())
                .maxHeadCount(recruitmentRegisterRequest.getParticipantStatusRegisterRequest().getMaxHeadCount())
                .startDate(recruitmentRegisterRequest.getPeriodRegisterRequest().getStartDate())
                .endDate(recruitmentRegisterRequest.getPeriodRegisterRequest().getEndDate())
                .build();
        recruitment.setState(NORMAL_RECRUITMENT_STATE);
        recruitment.setDeleted(NORMAL_DELETED);
        return recruitment;
    }

    public static RecruitmentRegisterRequest getNormalRegisterRequest() {

        ParticipantStatusRegisterRequest participantStatusRegisterRequest = ParticipantStatusSample
                .getNormalRegisterRequest();
        participantStatusRegisterRequest.setCurrentHeadCount(0);
        return RecruitmentRegisterRequest
                .builder()
                .periodRegisterRequest(PeriodSample.getNormalRegisterRequest())
                .participantStatusRegisterRequest(participantStatusRegisterRequest)
                .build();
    }

    public static final RecruitmentState NORMAL2_RECRUITMENT_STATE = RecruitmentState.PROCEEDING;
    public static final Boolean NORMAL2_DELETED = false;
    public static RecruitmentModifyRequest getNormal2ModifyRequest() {
        return RecruitmentModifyRequest.builder()
                .state(NORMAL2_RECRUITMENT_STATE)
                .participantStatusModifyRequest(ParticipantStatusSample.getNormal2ModifyRequest())
                .periodModifyRequest(PeriodSample.getNormal2ModifyRequest())
                .postModifyRequest(PostSample.getNormal2ModifyRequest())
                .build();
    }
}
