package com.BaGulBaGul.BaGulBaGul.domain.event.sampledata;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.LocationModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.LocationRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.sampledata.LocationSample;
import com.BaGulBaGul.BaGulBaGul.domain.common.sampledata.ParticipantStatusSample;
import com.BaGulBaGul.BaGulBaGul.domain.common.sampledata.PeriodSample;
import com.BaGulBaGul.BaGulBaGul.domain.event.Category;
import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.EventCategory;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.sampledata.PostSample;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;
import org.openapitools.jackson.nullable.JsonNullable;

public abstract class EventSample {
    //제약조건
    public static final int CATEGORY_MAX_COUNT = 2;
    //정상 이벤트 필드값
    public static final EventType NORMAL_EVENT_TYPE = EventType.LOCAL_EVENT;
    public static final Boolean NORMAL_DELETED = false;
    public static final Boolean NORMAL_AGE_LIMIT = false;
    public static final List<String> NORMAL_CATEGORIES = List.of("문화/예술", "식품/음료");

    //정상 이벤트
    public static Event getNormal() {
        EventRegisterRequest eventRegisterRequest = getNormalRegisterRequest();
        ParticipantStatusRegisterRequest participantStatusRegisterRequest = eventRegisterRequest
                .getParticipantStatusRegisterRequest();
        PeriodRegisterRequest periodRegisterRequest = eventRegisterRequest.getPeriodRegisterRequest();
        LocationRegisterRequest locationRegisterRequest = eventRegisterRequest.getLocationRegisterRequest();

        Event event = Event.builder()
                .type(NORMAL_EVENT_TYPE)
                .ageLimit(NORMAL_AGE_LIMIT)
                .currentHeadCount(participantStatusRegisterRequest.getCurrentHeadCount())
                .maxHeadCount(participantStatusRegisterRequest.getMaxHeadCount())
                .fullLocation(locationRegisterRequest.getFullLocation())
                .abstractLocation(locationRegisterRequest.getAbstractLocation())
                .latitudeLocation(locationRegisterRequest.getLatitudeLocation())
                .longitudeLocation(locationRegisterRequest.getLongitudeLocation())
                .startDate(periodRegisterRequest.getStartDate())
                .endDate(periodRegisterRequest.getEndDate())
                .build();
        //카테고리 추가
        List<EventCategory> categories = eventRegisterRequest.getCategories()
                .stream()
                .map(name -> Category.builder().name(name).build())
                .map(category -> new EventCategory(event, category))
                .collect(Collectors.toList());
        event.getCategories().addAll(categories);
        //삭제됨 필드 설정
        event.setDeleted(NORMAL_DELETED);
        return event;
    }


    //정상 이벤트 생성 요청
    public static EventRegisterRequest getNormalRegisterRequest() {
        ParticipantStatusRegisterRequest participantStatusRegisterRequest = ParticipantStatusSample
                .getNormalRegisterRequest();
        participantStatusRegisterRequest.setCurrentHeadCount(0);
        return EventRegisterRequest.builder()
                .type(NORMAL_EVENT_TYPE)
                .ageLimit(NORMAL_AGE_LIMIT)
                .categories(NORMAL_CATEGORIES)
                .participantStatusRegisterRequest(participantStatusRegisterRequest)
                .locationRegisterRequest(LocationSample.getNormalRegisterRequest())
                .periodRegisterRequest(PeriodSample.getNormalRegisterRequest())
                .postRegisterRequest(PostSample.getNormalRegisterRequest())
                .build();
    }

    public static EventModifyRequest getNormalModifyRequest() {
        return EventModifyRequest.builder()
                .type(NORMAL_EVENT_TYPE)
                .ageLimit(NORMAL_AGE_LIMIT)
                .categories(NORMAL_CATEGORIES)
                .participantStatusModifyRequest(ParticipantStatusSample.getNormalModifyRequest())
                .locationModifyRequest(LocationSample.getNormalModifyRequest())
                .periodModifyRequest(PeriodSample.getNormalModifyRequest())
                .postModifyRequest(PostSample.getNormalModifyRequest())
                .build();
    }

    public static final EventType NORMAL2_EVENT_TYPE = EventType.PARTY;
    public static final Boolean NORMAL2_AGE_LIMIT = true;
    public static final List<String> NORMAL2_CATEGORIES = List.of("문화/예술", "교육/체험");

    public static EventModifyRequest getNormal2ModifyRequest() {
        return EventModifyRequest.builder()
                .type(NORMAL2_EVENT_TYPE)
                .ageLimit(NORMAL2_AGE_LIMIT)
                .categories(NORMAL2_CATEGORIES)
                .participantStatusModifyRequest(ParticipantStatusSample.getNormal2ModifyRequest())
                .locationModifyRequest(LocationSample.getNormal2ModifyRequest())
                .periodModifyRequest(PeriodSample.getNormal2ModifyRequest())
                .postModifyRequest(PostSample.getNormal2ModifyRequest())
                .build();
    }
}
