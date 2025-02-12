package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
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
class EventService_IntegrationTest {
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
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest();

            //when
            Long eventId = eventService.registerEvent(user.getId(), eventRegisterRequest);
            Event event = eventRepository.findById(eventId).orElse(null);

            //then
            assertThat(event.getType()).isEqualTo(eventRegisterRequest.getType());
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
            User user = userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST);
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest();
            EventModifyRequest eventModifyRequest = EventSample.getNormal2ModifyRequest();
            Long eventId = eventService.registerEvent(user.getId(), eventRegisterRequest);

            //when
            eventService.modifyEvent(eventId, user.getId(), eventModifyRequest);
            entityManager.flush();
            entityManager.clear();
            Event event = eventRepository.findById(eventId).orElse(null);

            //then
            assertThat(event.getType()).isEqualTo(eventModifyRequest.getType());
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
            User user = userJoinService.registerUser(UserSample.NORMAL_USER_REGISTER_REQUEST);
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest();
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
}