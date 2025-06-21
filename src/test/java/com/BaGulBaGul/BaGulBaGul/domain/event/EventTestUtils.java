package com.BaGulBaGul.BaGulBaGul.domain.event;

import static org.assertj.core.api.Assertions.assertThat;

import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.LocationRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.ParticipantStatusRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.common.dto.request.PeriodRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailInfo;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleInfo;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.post.PostTestUtils;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.request.PostRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.post.dto.service.response.PostDetailInfo;

public class EventTestUtils {
    public static void assertEventDetailResponse(
            EventRegisterRequest eventRegisterRequest,
            EventDetailResponse eventDetailResponse
    ) {
        assertEventDetailInfo(eventRegisterRequest, eventDetailResponse.getEvent());
        PostTestUtils.assertPostDetailInfo(
                eventRegisterRequest.getPostRegisterRequest(),
                eventDetailResponse.getPost()
        );
    }

    public static void assertEventDetailInfo(
            EventRegisterRequest eventRegisterRequest,
            EventDetailInfo eventDetailInfo
    ) {
        //기본
        assertThat(eventDetailInfo.getType()).isEqualTo(eventRegisterRequest.getType());
        assertThat(eventDetailInfo.getEventHostUserId()).isEqualTo(eventRegisterRequest.getEventHostUserId());
        assertThat(eventDetailInfo.getAgeLimit()).isEqualTo(eventRegisterRequest.getAgeLimit());
        assertThat(eventDetailInfo.getCategories())
                .containsExactlyInAnyOrder(eventRegisterRequest.getCategories().toArray(String[]::new));

        //기간
        PeriodRegisterRequest periodRegisterRequest = eventRegisterRequest.getPeriodRegisterRequest();
        assertThat(eventDetailInfo.getStartDate()).isEqualTo(periodRegisterRequest.getStartDate());
        assertThat(eventDetailInfo.getEndDate()).isEqualTo(periodRegisterRequest.getEndDate());

        //장소
        LocationRegisterRequest locationRegisterRequest = eventRegisterRequest.getLocationRegisterRequest();
        assertThat(eventDetailInfo.getAbstractLocation()).isEqualTo(locationRegisterRequest.getAbstractLocation());
        assertThat(eventDetailInfo.getFullLocation()).isEqualTo(locationRegisterRequest.getFullLocation());
        assertThat(eventDetailInfo.getLongitudeLocation()).isEqualTo(locationRegisterRequest.getLongitudeLocation());
        assertThat(eventDetailInfo.getLatitudeLocation()).isEqualTo(locationRegisterRequest.getLatitudeLocation());

        //참가자
        ParticipantStatusRegisterRequest participantStatusRegisterRequest = eventRegisterRequest
                .getParticipantStatusRegisterRequest();
        assertThat(participantStatusRegisterRequest.getCurrentHeadCount())
                .isEqualTo(participantStatusRegisterRequest.getCurrentHeadCount());
        assertThat(participantStatusRegisterRequest.getMaxHeadCount())
                .isEqualTo(participantStatusRegisterRequest.getMaxHeadCount());
    }

    public static void assertEventSimpleResponse(
            EventRegisterRequest eventRegisterRequest,
            EventSimpleResponse eventSimpleResponse
    ) {
        assertEventSimpleInfo(eventRegisterRequest, eventSimpleResponse.getEvent());
        PostTestUtils.assertPostSimpleInfo(
                eventRegisterRequest.getPostRegisterRequest(),
                eventSimpleResponse.getPost()
        );
    }

    public static void assertEventSimpleInfo(
            EventRegisterRequest eventRegisterRequest,
            EventSimpleInfo eventSimpleInfo
    ) {
        //기본
        assertThat(eventSimpleInfo.getType()).isEqualTo(eventRegisterRequest.getType());
        assertThat(eventSimpleInfo.getEventHostUserId()).isEqualTo(eventRegisterRequest.getEventHostUserId());
        assertThat(eventSimpleInfo.getCategories())
                .containsExactlyInAnyOrder(eventRegisterRequest.getCategories().toArray(String[]::new));

        //기간
        PeriodRegisterRequest periodRegisterRequest = eventRegisterRequest.getPeriodRegisterRequest();
        assertThat(eventSimpleInfo.getStartDate()).isEqualTo(periodRegisterRequest.getStartDate());
        assertThat(eventSimpleInfo.getEndDate()).isEqualTo(periodRegisterRequest.getEndDate());

        //장소
        LocationRegisterRequest locationRegisterRequest = eventRegisterRequest.getLocationRegisterRequest();
        assertThat(eventSimpleInfo.getAbstractLocation()).isEqualTo(locationRegisterRequest.getAbstractLocation());

        //참가자
        ParticipantStatusRegisterRequest participantStatusRegisterRequest = eventRegisterRequest
                .getParticipantStatusRegisterRequest();
        assertThat(participantStatusRegisterRequest.getCurrentHeadCount())
                .isEqualTo(participantStatusRegisterRequest.getCurrentHeadCount());
        assertThat(participantStatusRegisterRequest.getMaxHeadCount())
                .isEqualTo(participantStatusRegisterRequest.getMaxHeadCount());
    }
}
