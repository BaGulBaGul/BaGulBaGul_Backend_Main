package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.EventTestUtils;
import com.BaGulBaGul.BaGulBaGul.domain.event.constant.EventType;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventConditionalRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventDetailResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.response.EventSimpleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.EventNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostImageService;
import com.BaGulBaGul.BaGulBaGul.domain.post.service.PostService;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.exception.NoPermissionException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        @Test
        @DisplayName("정상 등록")
        @Transactional
        void shouldOK() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.EVENT_HOST.name()))
                    .build();
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(eventHostUser.getId());

            //when
            Long eventId = eventService.registerEvent(authenticatedUserInfo, eventRegisterRequest);
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

        @ParameterizedTest
        @EnumSource(value = EventType.class)
        @DisplayName("이벤트 작성 권한이 없다면 거부하고, 있다면 허용해야 한다.")
        @Transactional
        void shouldRefuseAndAllowByPermission(EventType eventType) {
            //given
            //일반 USER 권한을 가진 유저
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.USER.name()))
                    .build();
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(eventHostUser.getId());
            //이벤트 타입만 변경
            eventRegisterRequest.setType(eventType);

            //when
            //then
            //FESTIVAL, LOCAL_EVENT는 권한이 필요
            if(eventType == EventType.FESTIVAL || eventType == EventType.LOCAL_EVENT) {
                assertThrows(NoPermissionException.class, () -> {
                    eventService.registerEvent(authenticatedUserInfo, eventRegisterRequest);
                });
            }
            //PARTY는 권한이 필요없음
            else {
                assertDoesNotThrow(() -> {
                    eventService.registerEvent(authenticatedUserInfo, eventRegisterRequest);
                });
            }
        }
    }

    @Nested
    @DisplayName("이벤트 수정 테스트")
    class EventModifyTest {

        @Test
        @DisplayName("작성한 이벤트 수정 - 전부 변경해야 함")
        @Transactional
        void shouldChangeAll() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.EVENT_HOST.name()))
                    .build();
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            User eventHostUser2 = userJoinService.registerUser(UserSample.getNormal3UserRegisterRequest());
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(eventHostUser.getId());
            EventModifyRequest eventModifyRequest = EventSample.getNormal2ModifyRequest(eventHostUser2.getId());
            eventModifyRequest.getPostModifyRequest().setImageIds(List.of(1L));
            Long eventId = eventService.registerEvent(authenticatedUserInfo, eventRegisterRequest);

            //when
            eventService.modifyEvent(authenticatedUserInfo, eventId, eventModifyRequest);
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
        @DisplayName("작성한 이벤트 수정 - 아무것도 변경하지 말아야 함")
        @Transactional
        void shouldChangeNothing() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.EVENT_HOST.name()))
                    .build();
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());

            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(eventHostUser.getId());
            EventModifyRequest eventModifyRequest = EventModifyRequest.builder().build();
            Long eventId = eventService.registerEvent(authenticatedUserInfo, eventRegisterRequest);

            //when
            eventService.modifyEvent(authenticatedUserInfo, eventId, eventModifyRequest);
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

        @Test
        @DisplayName("관리자는 다른 사람의 이벤트를 수정할 수 있다.")
        @Transactional
        void given_admin_can_modify_others_event() {
            //given
            User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
            User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            AuthenticatedUserInfo adminInfo = new AuthenticatedUserInfo(admin.getId(), List.of(GeneralRoleType.ADMIN.name()));

            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
            Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

            EventModifyRequest eventModifyRequest = EventSample.getNormal2ModifyRequest(writer.getId());

            //when
            eventService.modifyEvent(adminInfo, eventId, eventModifyRequest);

            //then
            entityManager.flush();
            entityManager.clear();
            Event event = eventRepository.findById(eventId).orElse(null);
            assertThat(event.getType()).isEqualTo(eventModifyRequest.getType());
        }

        @Test
        @DisplayName("일반유저는 다른 사람의 이벤트를 수정할 수 없다.")
        @Transactional
        void given_normal_user_cannot_modify_others_event() {
            //given
            User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
            User normalUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            AuthenticatedUserInfo normalUserInfo = new AuthenticatedUserInfo(normalUser.getId(), List.of(GeneralRoleType.USER.name()));

            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
            Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

            EventModifyRequest eventModifyRequest = EventSample.getNormal2ModifyRequest(writer.getId());

            //when
            //then
            assertThrows(NoPermissionException.class, () -> {
                eventService.modifyEvent(normalUserInfo, eventId, eventModifyRequest);
            });
        }

    }

    @Nested
    @DisplayName("삭제 테스트")
    class DeleteEventTest {
        @Test
        @DisplayName("관리자는 다른 사람의 이벤트를 삭제할 수 있다.")
        @Transactional
        void given_admin_can_delete_others_event() {
            //given
            User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
            User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            AuthenticatedUserInfo adminInfo = new AuthenticatedUserInfo(admin.getId(), List.of(GeneralRoleType.ADMIN.name()));

            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
            Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

            //when
            eventService.deleteEvent(adminInfo, eventId);

            //then
            entityManager.flush();
            entityManager.clear();
            Event event = eventRepository.findById(eventId).orElse(null);
            assertThat(event.getDeleted()).isTrue();
        }

        @Test
        @DisplayName("일반유저는 다른 사람의 이벤트를 삭제할 수 없다.")
        @Transactional
        void given_normal_user_cannot_delete_others_event() {
            //given
            User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
            User normalUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            AuthenticatedUserInfo normalUserInfo = new AuthenticatedUserInfo(normalUser.getId(), List.of(GeneralRoleType.USER.name()));

            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
            Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);

            //when
            //then
            assertThrows(NoPermissionException.class, () -> {
                eventService.deleteEvent(normalUserInfo, eventId);
            });
        }

        @Test
        @DisplayName("관리자는 삭제된 이벤트를 복구할 수 있다.")
        @Transactional
        void given_admin_can_restore_others_event() {
            //given
            User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
            User admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            AuthenticatedUserInfo adminInfo = new AuthenticatedUserInfo(admin.getId(), List.of(GeneralRoleType.ADMIN.name()));

            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
            Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);
            eventService.deleteEvent(writerInfo, eventId);
            entityManager.flush();
            entityManager.clear();

            //when
            eventService.restoreEvent(adminInfo, eventId);
            entityManager.flush();
            entityManager.clear();

            //then
            Event event = eventRepository.findById(eventId).orElse(null);
            assertThat(event.getDeleted()).isFalse();
        }

        @Test
        @DisplayName("일반 유저는 삭제된 이벤트를 복구할 수 없다.")
        @Transactional
        void given_normal_user_cannot_restore_others_event() {
            //given
            User writer = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
            User normalUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            AuthenticatedUserInfo writerInfo = new AuthenticatedUserInfo(writer.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            AuthenticatedUserInfo normalUserInfo = new AuthenticatedUserInfo(normalUser.getId(), List.of(GeneralRoleType.USER.name()));

            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(writer.getId());
            Long eventId = eventService.registerEvent(writerInfo, eventRegisterRequest);
            eventService.deleteEvent(writerInfo, eventId);
            entityManager.flush();
            entityManager.clear();

            //when
            //then
            assertThrows(NoPermissionException.class, () -> {
                eventService.restoreEvent(normalUserInfo, eventId);
            });
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
            AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.EVENT_HOST.name()))
                    .build();
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(eventHostUser.getId());
            Long eventId = eventService.registerEvent(
                    authenticatedUserInfo,
                    eventRegisterRequest
            );
            entityManager.flush();
            entityManager.clear();
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
            AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.EVENT_HOST.name()))
                    .build();
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            List<Long> eventIds = new ArrayList<>();

            int eventCount = 10;
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(eventHostUser.getId());
            for(int i = 0; i < eventCount; i++) {
                Long eventId = eventService.registerEvent(
                        authenticatedUserInfo,
                        eventRegisterRequest
                );
                eventIds.add(eventId);
            }
            entityManager.flush();
            entityManager.clear();
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
        @Test
        @DisplayName("삭제된 이벤트 단일 상세 조회 테스트")
        @Transactional
        void readSingleEventDetail_deleted() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.EVENT_HOST.name()))
                    .build();
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(eventHostUser.getId());
            Long eventId = eventService.registerEvent(
                    authenticatedUserInfo,
                    eventRegisterRequest
            );
            eventService.deleteEvent(authenticatedUserInfo, eventId);
            entityManager.flush();
            entityManager.clear();

            //when
            //then
            assertThrows(EventNotFoundException.class, () -> {
                eventService.getEventDetailById(eventId);
            });
        }

        @Test
        @DisplayName("이벤트 페이지 조회 - 삭제되지 않은 이벤트")
        @Transactional
        void getEventPageByCondition_notDeleted() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.EVENT_HOST.name()))
                    .build();
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            EventRegisterRequest eventRegisterRequest1 = EventSample.getNormalRegisterRequest(eventHostUser.getId());
            Long eventId1 = eventService.registerEvent(authenticatedUserInfo, eventRegisterRequest1);
            EventRegisterRequest eventRegisterRequest2 = EventSample.getNormal2RegisterRequest(eventHostUser.getId());
            Long eventId2 = eventService.registerEvent(authenticatedUserInfo, eventRegisterRequest2);

            eventService.deleteEvent(authenticatedUserInfo, eventId2);
            entityManager.flush();
            entityManager.clear();

            //when
            EventConditionalRequest eventConditionalRequest = new EventConditionalRequest();
            eventConditionalRequest.setDeleted(false);
            Page<EventSimpleResponse> page = eventService.getEventPageByCondition(
                    eventConditionalRequest, PageRequest.of(0, 10));

            //then
            assertThat(page.getContent().size()).isEqualTo(1);
            assertThat(page.getContent().get(0).getEvent().getEventId()).isEqualTo(eventId1);
        }

        @Test
        @DisplayName("이벤트 페이지 조회 - 삭제된 이벤트")
        @Transactional
        void getEventPageByCondition_deleted() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.EVENT_HOST.name()))
                    .build();
            User eventHostUser = userJoinService.registerUser(UserSample.getNormal2UserRegisterRequest());
            EventRegisterRequest eventRegisterRequest1 = EventSample.getNormalRegisterRequest(eventHostUser.getId());
            Long eventId1 = eventService.registerEvent(authenticatedUserInfo, eventRegisterRequest1);
            EventRegisterRequest eventRegisterRequest2 = EventSample.getNormal2RegisterRequest(eventHostUser.getId());
            Long eventId2 = eventService.registerEvent(authenticatedUserInfo, eventRegisterRequest2);

            eventService.deleteEvent(authenticatedUserInfo, eventId2);
            entityManager.flush();
            entityManager.clear();

            //when
            EventConditionalRequest eventConditionalRequest = new EventConditionalRequest();
            eventConditionalRequest.setDeleted(true);
            Page<EventSimpleResponse> page = eventService.getEventPageByCondition(
                    eventConditionalRequest, PageRequest.of(0, 10));

            //then
            assertThat(page.getContent().size()).isEqualTo(1);
            assertThat(page.getContent().get(0).getEvent().getEventId()).isEqualTo(eventId2);
        }
    }
}
