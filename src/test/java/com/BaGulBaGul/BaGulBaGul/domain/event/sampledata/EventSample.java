package com.BaGulBaGul.BaGulBaGul.domain.event.sampledata;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.LocationRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public abstract class EventSample {
    public static final EventType NORMAL_EVENT_TYPE = EventType.LOCAL_EVENT;
    public static final Integer NORMAL_MAX_HEAD_COUNT = 8;
    public static final Integer NORMAL_CURRENT_HEAD_COUNT = 3;
    public static final String NORMAL_FULL_LOCATION = "서울시 영등포구 xxx로 xxx타워 x층";
    public static final String NORMAL_ABSTRACT_LOCATION = "서울시 영등포구";
    public static final Float NORMAL_LATITUDE_LOCATION = 33.83f;
    public static final Float NORMAL_LONGITUDE_LOCATION = 40.23f;
    public static final Boolean NORMAL_AGE_LIMIT = false;
    public static final LocalDateTime NORMAL_START_DATE = LocalDateTime.of(
            2024, Month.NOVEMBER, 13, 7, 00
    );
    public static final LocalDateTime NORMAL_END_DATE = LocalDateTime.of(
            2024, Month.NOVEMBER, 15, 17, 00
    );
    public static final List<String> NORMAL_CATEGORIES = List.of("문화/예술", "식품/음료");

    public static EventRegisterRequest NORMAL_REGISTER_REQUEST = EventRegisterRequest.builder()
            .type(NORMAL_EVENT_TYPE)
            .ageLimit(NORMAL_AGE_LIMIT)
            .categories(NORMAL_CATEGORIES)
            .participantStatusRegisterRequest(
                    ParticipantStatusRegisterRequest.builder()
                            .currentHeadCount(null)
                            .maxHeadCount(NORMAL_MAX_HEAD_COUNT)
                            .build()
            )
            .locationRegisterRequest(
                    LocationRegisterRequest.builder()
                            .fullLocation(NORMAL_FULL_LOCATION)
                            .abstractLocation(NORMAL_ABSTRACT_LOCATION)
                            .latitudeLocation(NORMAL_LATITUDE_LOCATION)
                            .longitudeLocation(NORMAL_LONGITUDE_LOCATION)
                            .build()
            )
            .periodRegisterRequest(
                    PeriodRegisterRequest.builder()
                            .startDate(NORMAL_START_DATE)
                            .endDate(NORMAL_END_DATE)
                            .build()
            )
            .build();

    public static final EventType NORMAL2_EVENT_TYPE = EventType.PARTY;
    public static final Integer NORMAL2_MAX_HEAD_COUNT = 15;
    public static final Integer NORMAL2_CURRENT_HEAD_COUNT = 8;
    public static final String NORMAL2_FULL_LOCATION = "경기도 고양시 xxx로 xxx타워 x층";
    public static final String NORMAL2_ABSTRACT_LOCATION = "경기도 고양시";
    public static final Float NORMAL2_LATITUDE_LOCATION = 30.83f;
    public static final Float NORMAL2_LONGITUDE_LOCATION = 55.23f;
    public static final Boolean NORMAL2_AGE_LIMIT = true;
    public static final LocalDateTime NORMAL2_START_DATE = LocalDateTime.of(
            2024, Month.NOVEMBER, 15, 7, 00
    );
    public static final LocalDateTime NORMAL2_END_DATE = LocalDateTime.of(
            2024, Month.NOVEMBER, 17, 17, 00
    );
    public static final List<String> NORMAL2_CATEGORIES = List.of("문화/예술", "교육/체험");
}
