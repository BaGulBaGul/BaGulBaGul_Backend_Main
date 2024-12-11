package com.BaGulBaGul.BaGulBaGul.domain.common.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodRegisterRequest;
import java.time.LocalDateTime;
import java.time.Month;
import org.openapitools.jackson.nullable.JsonNullable;

public class PeriodSample {
    public static final LocalDateTime NORMAL_START_DATE = LocalDateTime.of(
            2024, Month.NOVEMBER, 13, 7, 00
    );
    public static final LocalDateTime NORMAL_END_DATE = LocalDateTime.of(
            2024, Month.NOVEMBER, 15, 17, 00
    );
    public static PeriodRegisterRequest getNormalRegisterRequest() {
        return PeriodRegisterRequest.builder()
                .startDate(NORMAL_START_DATE)
                .endDate(NORMAL_END_DATE)
                .build();
    }
    public static PeriodModifyRequest getNormalModifyRequest() {
        return PeriodModifyRequest.builder()
                .startDate(JsonNullable.of(NORMAL_START_DATE))
                .endDate(JsonNullable.of(NORMAL_END_DATE))
                .build();
    }
    public static final LocalDateTime NORMAL2_START_DATE = LocalDateTime.of(
            2024, Month.NOVEMBER, 15, 7, 00
    );
    public static final LocalDateTime NORMAL2_END_DATE = LocalDateTime.of(
            2024, Month.NOVEMBER, 17, 17, 00
    );
    public static PeriodRegisterRequest getNormal2RegisterRequest() {
        return PeriodRegisterRequest.builder()
                .startDate(NORMAL2_START_DATE)
                .endDate(NORMAL2_END_DATE)
                .build();
    }
    public static PeriodModifyRequest getNormal2ModifyRequest() {
        return PeriodModifyRequest.builder()
                .startDate(JsonNullable.of(NORMAL_START_DATE))
                .endDate(JsonNullable.of(NORMAL_END_DATE))
                .build();
    }
}
