package com.BaGulBaGul.BaGulBaGul.domain.common.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusRegisterRequest;
import org.openapitools.jackson.nullable.JsonNullable;

public class ParticipantStatusSample {
    public static final Integer NORMAL_MAX_HEAD_COUNT = 8;
    public static final Integer NORMAL_CURRENT_HEAD_COUNT = 3;
    public static ParticipantStatusRegisterRequest getNormalRegisterRequest() {
        return ParticipantStatusRegisterRequest.builder()
                .maxHeadCount(NORMAL_MAX_HEAD_COUNT)
                .currentHeadCount(NORMAL_CURRENT_HEAD_COUNT)
                .build();
    }
    public static ParticipantStatusModifyRequest getNormalModifyRequest() {
        return ParticipantStatusModifyRequest.builder()
                .maxHeadCount(JsonNullable.of(NORMAL_MAX_HEAD_COUNT))
                .currentHeadCount(JsonNullable.of(NORMAL_CURRENT_HEAD_COUNT))
                .build();
    }
    public static final Integer NORMAL2_MAX_HEAD_COUNT = 15;
    public static final Integer NORMAL2_CURRENT_HEAD_COUNT = 8;
    public static final ParticipantStatusRegisterRequest getNormal2RegisterRequest() {
        return ParticipantStatusRegisterRequest.builder()
                .maxHeadCount(NORMAL2_MAX_HEAD_COUNT)
                .currentHeadCount(NORMAL2_CURRENT_HEAD_COUNT)
                .build();
    }
    public static ParticipantStatusModifyRequest getNormal2ModifyRequest() {
        return ParticipantStatusModifyRequest.builder()
                .maxHeadCount(JsonNullable.of(NORMAL2_MAX_HEAD_COUNT))
                .currentHeadCount(JsonNullable.of(NORMAL2_CURRENT_HEAD_COUNT))
                .build();
    }
}
