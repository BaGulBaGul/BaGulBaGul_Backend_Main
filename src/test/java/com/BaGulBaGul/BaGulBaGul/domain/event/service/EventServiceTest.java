package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import static com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostService;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.info.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("test2")
class EventServiceTest {
    @SpyBean
    EventService eventService;

    @Autowired
    UserJoinService userJoinService;

    @MockBean
    PostService postService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EntityManager entityManager;

    @Nested
    @DisplayName("이벤트 등록 테스트")
    class EventRegisterTest {

        @Test
        @DisplayName("정상 등록")
        @Transactional
        void shouldOK() {
            //given
            User user = userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST);
            EventRegisterRequest eventRegisterRequest = EventRegisterRequest.builder()
                    .type(NORMAL_EVENT_TYPE)
                    .maxHeadCount(NORMAL_MAX_HEAD_COUNT)
                    .fullLocation(NORMAL_FULL_LOCATION)
                    .abstractLocation(NORMAL_ABSTRACT_LOCATION)
                    .latitudeLocation(NORMAL_LATITUDE_LOCATION)
                    .longitudeLocation(NORMAL_LONGITUDE_LOCATION)
                    .ageLimit(NORMAL_AGE_LIMIT)
                    .startDate(NORMAL_START_DATE)
                    .endDate(NORMAL_END_DATE)
                    .categories(NORMAL_CATEGORIES)
                    .build();

            //when
            Long eventId = eventService.registerEvent(user.getId(), eventRegisterRequest);
            Event event = eventRepository.findById(eventId).orElse(null);

            //then
            assertThat(event.getType()).isEqualTo(eventRegisterRequest.getType());
            assertThat(event.getMaxHeadCount()).isEqualTo(eventRegisterRequest.getMaxHeadCount());
            assertThat(event.getFullLocation()).isEqualTo(eventRegisterRequest.getFullLocation());
            assertThat(event.getAbstractLocation()).isEqualTo(eventRegisterRequest.getAbstractLocation());
            assertThat(event.getLatitudeLocation()).isEqualTo(eventRegisterRequest.getLatitudeLocation());
            assertThat(event.getLongitudeLocation()).isEqualTo(eventRegisterRequest.getLongitudeLocation());
            assertThat(event.getAgeLimit()).isEqualTo(eventRegisterRequest.getAgeLimit());
            assertThat(event.getStartDate()).isEqualTo(eventRegisterRequest.getStartDate());
            assertThat(event.getEndDate()).isEqualTo(eventRegisterRequest.getEndDate());
            assertThat(event.getCategories().stream().map(x -> x.getCategory().getName()).collect(Collectors.toList()))
                    .contains(eventRegisterRequest.getCategories().toArray(new String[0]));
            verify(postService, times(1)).registerPost(eq(user), any());
        }
    }

    @Nested
    @DisplayName("이벤트 수정 테스트")
    class EventModifyTest {

        @BeforeEach
        void init() {
            //쓰기 권한 확인 무효화
            doNothing().when(eventService).checkWritePermission(any(), any());
        }

        @Test
        @DisplayName("전부 변경해야 함")
        @Transactional
        void shouldChangeAll() {
            //given
            User user = userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST);
            EventRegisterRequest eventRegisterRequest = EventRegisterRequest.builder()
                    .type(NORMAL_EVENT_TYPE)
                    .maxHeadCount(NORMAL_MAX_HEAD_COUNT)
                    .fullLocation(NORMAL_FULL_LOCATION)
                    .abstractLocation(NORMAL_ABSTRACT_LOCATION)
                    .latitudeLocation(NORMAL_LATITUDE_LOCATION)
                    .longitudeLocation(NORMAL_LONGITUDE_LOCATION)
                    .ageLimit(NORMAL_AGE_LIMIT)
                    .startDate(NORMAL_START_DATE)
                    .endDate(NORMAL_END_DATE)
                    .categories(NORMAL_CATEGORIES)
                    .build();
            EventModifyRequest eventModifyRequest = EventModifyRequest.builder()
                    .type(NORMAL2_EVENT_TYPE)
                    .maxHeadCount(JsonNullable.of(NORMAL2_MAX_HEAD_COUNT))
                    .currentHeadCount(JsonNullable.of(NORMAL2_CURRENT_HEAD_COUNT))
                    .fullLocation(NORMAL2_FULL_LOCATION)
                    .abstractLocation(NORMAL2_ABSTRACT_LOCATION)
                    .latitudeLocation(NORMAL2_LATITUDE_LOCATION)
                    .longitudeLocation(NORMAL2_LONGITUDE_LOCATION)
                    .ageLimit(NORMAL2_AGE_LIMIT)
                    .startDate(NORMAL2_START_DATE)
                    .endDate(NORMAL2_END_DATE)
                    .categories(NORMAL2_CATEGORIES)
                    .build();
            Long eventId = eventService.registerEvent(user.getId(), eventRegisterRequest);

            //when
            eventService.modifyEvent(eventId, user.getId(), eventModifyRequest);
            entityManager.flush();
            entityManager.clear();
            Event event = eventRepository.findById(eventId).orElse(null);

            //then
            assertThat(event.getType()).isEqualTo(eventModifyRequest.getType());
            assertThat(event.getMaxHeadCount()).isEqualTo(eventModifyRequest.getMaxHeadCount().get());
            assertThat(event.getCurrentHeadCount()).isEqualTo(eventModifyRequest.getCurrentHeadCount().get());
            assertThat(event.getFullLocation()).isEqualTo(eventModifyRequest.getFullLocation());
            assertThat(event.getAbstractLocation()).isEqualTo(eventModifyRequest.getAbstractLocation());
            assertThat(event.getLatitudeLocation()).isEqualTo(eventModifyRequest.getLatitudeLocation());
            assertThat(event.getLongitudeLocation()).isEqualTo(eventModifyRequest.getLongitudeLocation());
            assertThat(event.getAgeLimit()).isEqualTo(eventModifyRequest.getAgeLimit());
            assertThat(event.getStartDate()).isEqualTo(eventModifyRequest.getStartDate());
            assertThat(event.getEndDate()).isEqualTo(eventModifyRequest.getEndDate());
            assertThat(event.getCategories().stream().map(x -> x.getCategory().getName()).collect(Collectors.toList()))
                    .contains(eventModifyRequest.getCategories().toArray(new String[0]));
            verify(postService, times(1)).modifyPost(any(), any());
        }

        @Test
        @DisplayName("아무것도 변경하지 말아야 함")
        @Transactional
        void shouldChangeNothing() {
            //given
            User user = userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST);
            EventRegisterRequest eventRegisterRequest = EventRegisterRequest.builder()
                    .type(NORMAL_EVENT_TYPE)
                    .maxHeadCount(NORMAL_MAX_HEAD_COUNT)
                    .fullLocation(NORMAL_FULL_LOCATION)
                    .abstractLocation(NORMAL_ABSTRACT_LOCATION)
                    .latitudeLocation(NORMAL_LATITUDE_LOCATION)
                    .longitudeLocation(NORMAL_LONGITUDE_LOCATION)
                    .ageLimit(NORMAL_AGE_LIMIT)
                    .startDate(NORMAL_START_DATE)
                    .endDate(NORMAL_END_DATE)
                    .categories(NORMAL_CATEGORIES)
                    .build();
            EventModifyRequest eventModifyRequest = EventModifyRequest.builder()
                    .build();
            Long eventId = eventService.registerEvent(user.getId(), eventRegisterRequest);

            //when
            eventService.modifyEvent(eventId, user.getId(), eventModifyRequest);
            entityManager.flush();
            entityManager.clear();
            Event event = eventRepository.findById(eventId).orElse(null);

            //then
            assertThat(event.getType()).isEqualTo(eventRegisterRequest.getType());
            assertThat(event.getMaxHeadCount()).isEqualTo(eventRegisterRequest.getMaxHeadCount());
            assertThat(event.getFullLocation()).isEqualTo(eventRegisterRequest.getFullLocation());
            assertThat(event.getAbstractLocation()).isEqualTo(eventRegisterRequest.getAbstractLocation());
            assertThat(event.getLatitudeLocation()).isEqualTo(eventRegisterRequest.getLatitudeLocation());
            assertThat(event.getLongitudeLocation()).isEqualTo(eventRegisterRequest.getLongitudeLocation());
            assertThat(event.getAgeLimit()).isEqualTo(eventRegisterRequest.getAgeLimit());
            assertThat(event.getStartDate()).isEqualTo(eventRegisterRequest.getStartDate());
            assertThat(event.getEndDate()).isEqualTo(eventRegisterRequest.getEndDate());
            assertThat(event.getCategories().stream().map(x -> x.getCategory().getName()).collect(Collectors.toList()))
                    .contains(eventRegisterRequest.getCategories().toArray(new String[0]));
            verify(postService, times(1)).modifyPost(any(), any());
        }
    }
}