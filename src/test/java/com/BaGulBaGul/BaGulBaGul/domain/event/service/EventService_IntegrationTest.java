package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.EventTestUtils;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostImageService;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostService;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class EventService_IntegrationTest {
    @SpyBean
    EventService eventService;

    @Autowired
    UserJoinService userJoinService;

    @SpyBean
    PostService postService;

    @MockBean
    PostImageService postImageService;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EntityManager entityManager;



    @Nested
    @DisplayName("이벤트 등록 테스트")
    class EventRegisterTest {
        @BeforeEach
        void init() {
            doNothing().when(eventService).checkWritePermission(any(), any());

        }

        @Test
        @DisplayName("정상 등록")
        @Transactional
        void shouldOK() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(eventHostUser.getId());

            //when
            Long eventId = eventService.registerEvent(user.getId(), eventRegisterRequest);
            Event event = eventRepository.findById(eventId).orElse(null);

            //then
            assertThat(event.getType()).isEqualTo(eventRegisterRequest.getType());
            assertThat(event.getHostUser().getId()).isEqualTo(eventHostUser.getId());
            assertThat(event.getAgeLimit()).isEqualTo(eventRegisterRequest.getAgeLimit());
            assertThat(event.getMaxHeadCount()).isEqualTo(eventRegisterRequest.getParticipantStatusRegisterRequest().getMaxHeadCount());
            assertThat(event.getCurrentHeadCount()).isEqualTo(eventRegisterRequest.getParticipantStatusRegisterRequest().getCurrentHeadCount());
            assertThat(event.getCategories().stream().map(x -> x.getCategory().getName()).collect(Collectors.toList()))
                    .contains(eventRegisterRequest.getCategories().toArray(new String[0]));
            assertThat(event.getFullLocation()).isEqualTo(eventRegisterRequest.getLocationRegisterRequest().getFullLocation());
            assertThat(event.getAbstractLocation()).isEqualTo(eventRegisterRequest.getLocationRegisterRequest().getAbstractLocation());
            assertThat(event.getLatitudeLocation()).isEqualTo(eventRegisterRequest.getLocationRegisterRequest().getLatitudeLocation());
            assertThat(event.getLongitudeLocation()).isEqualTo(eventRegisterRequest.getLocationRegisterRequest().getLongitudeLocation());
            assertThat(event.getStartDate()).isEqualTo(eventRegisterRequest.getPeriodRegisterRequest().getStartDate());
            assertThat(event.getEndDate()).isEqualTo(eventRegisterRequest.getPeriodRegisterRequest().getEndDate());
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
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            User eventHostUser2 = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(eventHostUser.getId());
            EventModifyRequest eventModifyRequest = EventSample.getNormal2ModifyRequest(eventHostUser2.getId());
            eventModifyRequest.getPostModifyRequest().setImageIds(List.of(1L));
            Long eventId = eventService.registerEvent(user.getId(), eventRegisterRequest);

            //when
            eventService.modifyEvent(eventId, user.getId(), eventModifyRequest);
            entityManager.flush();
            entityManager.clear();
            Event event = eventRepository.findById(eventId).orElse(null);

            //then
            assertThat(event.getType()).isEqualTo(eventModifyRequest.getType());
            assertThat(event.getHostUser().getId()).isEqualTo(eventModifyRequest.getEventHostUserId().get());
            assertThat(event.getAgeLimit()).isEqualTo(eventModifyRequest.getAgeLimit());
            assertThat(event.getCategories().stream()
                    .map(x -> x.getCategory().getName()).collect(Collectors.toList()))
                    .contains(eventModifyRequest.getCategories().toArray(new String[0]));

            assertThat(event.getFullLocation())
                    .isEqualTo(eventModifyRequest.getLocationModifyRequest().getFullLocation().get());
            assertThat(event.getAbstractLocation())
                    .isEqualTo(eventModifyRequest.getLocationModifyRequest().getAbstractLocation().get());
            assertThat(event.getLatitudeLocation())
                    .isEqualTo(eventModifyRequest.getLocationModifyRequest().getLatitudeLocation().get());
            assertThat(event.getLongitudeLocation())
                    .isEqualTo(eventModifyRequest.getLocationModifyRequest().getLongitudeLocation().get());

            assertThat(event.getMaxHeadCount())
                    .isEqualTo(eventModifyRequest.getParticipantStatusModifyRequest().getMaxHeadCount().get());
            assertThat(event.getCurrentHeadCount())
                    .isEqualTo(eventModifyRequest.getParticipantStatusModifyRequest().getCurrentHeadCount().get());

            assertThat(event.getStartDate())
                    .isEqualTo(eventModifyRequest.getPeriodModifyRequest().getStartDate().get());
            assertThat(event.getEndDate())
                    .isEqualTo(eventModifyRequest.getPeriodModifyRequest().getEndDate().get());

            verify(postService, times(1)).modifyPost(any(), any());
        }

        @Test
        @DisplayName("아무것도 변경하지 말아야 함")
        @Transactional
        void shouldChangeNothing() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());

            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(eventHostUser.getId());
            EventModifyRequest eventModifyRequest = EventModifyRequest.builder().build();
            Long eventId = eventService.registerEvent(user.getId(), eventRegisterRequest);

            //when
            eventService.modifyEvent(eventId, user.getId(), eventModifyRequest);
            entityManager.flush();
            entityManager.clear();
            Event event = eventRepository.findById(eventId).orElse(null);

            //then
            assertThat(event.getType()).isEqualTo(eventRegisterRequest.getType());
            assertThat(event.getHostUser().getId()).isEqualTo(eventRegisterRequest.getEventHostUserId());
            assertThat(event.getAgeLimit()).isEqualTo(eventRegisterRequest.getAgeLimit());
            assertThat(event.getCategories().stream()
                    .map(x -> x.getCategory().getName()).collect(Collectors.toList()))
                    .contains(eventRegisterRequest.getCategories().toArray(new String[0]));

            assertThat(event.getFullLocation())
                    .isEqualTo(eventRegisterRequest.getLocationRegisterRequest().getFullLocation());
            assertThat(event.getAbstractLocation())
                    .isEqualTo(eventRegisterRequest.getLocationRegisterRequest().getAbstractLocation());
            assertThat(event.getLatitudeLocation())
                    .isEqualTo(eventRegisterRequest.getLocationRegisterRequest().getLatitudeLocation());
            assertThat(event.getLongitudeLocation())
                    .isEqualTo(eventRegisterRequest.getLocationRegisterRequest().getLongitudeLocation());

            assertThat(event.getMaxHeadCount())
                    .isEqualTo(eventRegisterRequest.getParticipantStatusRegisterRequest().getMaxHeadCount());

            assertThat(event.getStartDate()).isEqualTo(eventRegisterRequest.getPeriodRegisterRequest().getStartDate());
            assertThat(event.getEndDate()).isEqualTo(eventRegisterRequest.getPeriodRegisterRequest().getEndDate());

            verify(postService, times(1)).modifyPost(any(), any());
        }
    }

    @Nested
    @DisplayName("조회 테스트")
    class EventReadTest {
        @Test
        @DisplayName("이벤트 단일 상세 조회 테스트")
        @Transactional
        void readSingleEventDetailTest() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(eventHostUser.getId());
            Long eventId = eventService.registerEvent(
                    user.getId(),
                    eventRegisterRequest
            );
            //when
            EventDetailResponse eventDetailResponse = eventService.getEventDetailById(eventId);
            //then
            EventTestUtils.assertEventDetailResponse(
                    eventRegisterRequest,
                    eventDetailResponse
            );
        }

        @Test
        @DisplayName("이벤트 다수 간략 조회 테스트")
        @Transactional
        void readManyEventSimpleTest() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            List<Long> eventIds = new ArrayList<>();

            int eventCount = 10;
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(eventHostUser.getId());
            for(int i = 0; i < eventCount; i++) {
                Long eventId = eventService.registerEvent(
                        user.getId(),
                        eventRegisterRequest
                );
                eventIds.add(eventId);
            }
            //when
            List<EventSimpleResponse> eventSimpleResponses = eventService.getEventSimpleResponseByIds(eventIds);
            //then
            for(EventSimpleResponse eventDetailResponse : eventSimpleResponses) {
                EventTestUtils.assertEventSimpleResponse(
                        eventRegisterRequest,
                        eventDetailResponse
                );
            }
        }
    }
}