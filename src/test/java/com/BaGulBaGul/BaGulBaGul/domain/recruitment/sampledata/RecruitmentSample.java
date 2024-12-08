package com.BaGulBaGul.BaGulBaGul.domain.recruitment.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.constant.RecruitmentState;
import com.BaGulBaGul.BaGulBaGul.domain.recruitment.dto.service.request.RecruitmentRegisterRequest;
import java.time.LocalDateTime;
import java.time.Month;

public class RecruitmentSample {
    public static final RecruitmentState NORMAL_RECRUITMENT_STATE = RecruitmentState.PROCEEDING;
    public static final Integer NORMAL_CURRENT_HEAD_COUNT = 0;
    public static final Integer NORMAL_MAX_HEAD_COUNT = 8;
    public static final LocalDateTime NORMAL_START_DATE = LocalDateTime.of(
            2024, Month.NOVEMBER, 13, 7, 00);
    public static final LocalDateTime NORMAL_END_DATE = LocalDateTime.of(
            2024, Month.NOVEMBER, 15, 17, 00);
    public static final RecruitmentRegisterRequest NORMAL_REGISTER_REQUEST = RecruitmentRegisterRequest
            .builder()
            .periodRegisterRequest(PeriodRegisterRequest.builder()
                    .startDate(NORMAL_START_DATE)
                    .endDate(NORMAL_END_DATE)
                    .build())
            .participantStatusRegisterRequest(ParticipantStatusRegisterRequest.builder()
                    .currentHeadCount(NORMAL_CURRENT_HEAD_COUNT)
                    .maxHeadCount(NORMAL_MAX_HEAD_COUNT)
                    .build())
            .build();

    public static final RecruitmentState NORMAL2_RECRUITMENT_STATE = RecruitmentState.PROCEEDING;
    public static final Integer NORMAL2_CURRENT_HEAD_COUNT = 3;
    public static final Integer NORMAL2_MAX_HEAD_COUNT = 8;
    public static final LocalDateTime NORMAL2_START_DATE = LocalDateTime.of(
            2024, Month.NOVEMBER, 16, 12, 00);
    public static final LocalDateTime NORMAL2_END_DATE = LocalDateTime.of(
            2024, Month.NOVEMBER, 20, 22, 00);
}
