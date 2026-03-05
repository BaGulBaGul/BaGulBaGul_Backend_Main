package com.BaGulBaGul.BaGulBaGul.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.BaGulBaGul.BaGulBaGul.domain.calendar.EventCalendar;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.repository.EventCalendarRepository;
import com.BaGulBaGul.BaGulBaGul.domain.calendar.event.service.EventCalendarService;
import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.UserTestUtils;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.api.response.OtherUserInfoApiResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserModifyRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.EventHostUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.MyUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.OtherUserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.UserInfoResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.exception.DuplicateUsernameException;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import java.util.Calendar;
import java.util.List;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserInfoService_IntegrationTest {

    @MockBean
    UserImageService userImageService;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    UserJoinService userJoinService;

    @Autowired
    EventService eventService;

    @Autowired
    EventCalendarService eventCalendarService;


    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventCalendarRepository eventCalendarRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    void init() {
        doNothing().when(userImageService).setImage(any(), any());
    }

    @Nested
    @DisplayName("유저 조회 테스트")
    class getUserInfoTest {

        User user;
        @BeforeEach
        void init() {
            //유저 생성
            user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                    .userId(user.getId())
                    .roles(List.of(GeneralRoleType.ADMIN.name(), GeneralRoleType.EVENT_HOST.name()))
                    .build();
            //이벤트 작성
            Long eventId = eventService.registerEvent(authenticatedUserInfo,
                    EventSample.getNormalRegisterRequest(user.getId()));
            Event event = eventRepository.findById(eventId).get();
            //캘린더 1개 추가
            eventCalendarService.registerEventCalendar(user.getId(), eventId);
            //좋아요 1개 추가
            eventService.addLike(eventId, user.getId());
            //영속성 초기화
            entityManager.flush();
            entityManager.clear();
        }

        @Test
        @DisplayName("유저 정보 조회 테스트")
        void getUserInfoTest() {
            //when
            UserInfoResponse userInfoResponse = userInfoService.getUserInfo(user.getId());
            //then
            user = userRepository.findById(user.getId()).get();
            UserTestUtils.assertUserInfoResponse(userInfoResponse, user);
        }

        @Test
        @DisplayName("자기 정보 조회 테스트")
        void getMyUserInfoTest() {
            //when
            MyUserInfoResponse myUserInfoResponse = userInfoService.getMyUserInfo(user.getId());
            //then
            user = userRepository.findById(user.getId()).get();
            UserTestUtils.assertMyUserInfoResponse(myUserInfoResponse, user, 1L, 1L, 1L);
        }

        @Test
        @DisplayName("다른 유저 정보 조회 테스트")
        void getOtherUserInfoTest() {
            //when
            OtherUserInfoResponse otherUserInfoResponse = userInfoService.getOtherUserInfo(user.getId());
            //then
            user = userRepository.findById(user.getId()).get();
            UserTestUtils.assertOtherUserInfoResponse(otherUserInfoResponse, user, 1L);
        }

        @Test
        @DisplayName("이벤트 호스트 유저 정보 조회 테스트")
        void getEventHostUserInfoTest() {
            //when
            EventHostUserInfoResponse eventHostUserInfoResponse = userInfoService.getEventHostUserInfo(user.getId());
            //then
            user = userRepository.findById(user.getId()).get();
            UserTestUtils.assertEventHostUserInfoResponse(eventHostUserInfoResponse, user, 0L, 1L, 0L);
        }
    }
    @Nested
    @DisplayName("유저 수정 테스트")
    class modifyUserInfoTest {

        @Test
        @DisplayName("정상 동작")
        void shouldOK() {
            //given
            User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
            UserModifyRequest normal2UserModifyRequest = UserSample.getNormal2UserModifyRequest();
            normal2UserModifyRequest.setImageResourceId(JsonNullable.of(1L));
            //when
            userInfoService.modifyUserInfo(normal2UserModifyRequest, user.getId());
            //수정한 내용을 db에 즉시 반영하고 영속성 컨텍스트 초기화
            entityManager.flush();
            entityManager.clear();
            user = userRepository.findById(user.getId()).orElse(null);

            //then
            assertThat(user.getNickname()).isEqualTo(normal2UserModifyRequest.getUsername().get());
            assertThat(user.getEmail()).isEqualTo(normal2UserModifyRequest.getEmail().get());
            assertThat(user.getProfileMessage()).isEqualTo(normal2UserModifyRequest.getProfileMessage().get());
            verify(userImageService, times(1)).setImage(any(), any());
        }

        @Test
        @DisplayName("유저명이 기존 유저의 유저명과 정확히 일치")
        void shouldThrowDuplicateUsernameException_WhenUsernameExactlySame() {
            //given
            UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
            UserModifyRequest userModifyRequest = UserSample.getNormal2UserModifyRequest();
            userModifyRequest.setUsername(JsonNullable.of(userRegisterRequest.getNickname()));
            User user = userJoinService.registerUser(userRegisterRequest);
            //when then
            assertThatThrownBy(() -> {
                userInfoService.modifyUserInfo(userModifyRequest, user.getId());
                //수정한 내용을 db에 즉시 반영하고 영속성 컨텍스트 초기화
                entityManager.flush();
                entityManager.clear();
            }).isInstanceOf(DuplicateUsernameException.class);
        }

        @Test
        @DisplayName("유저명이 기존 유저의 유저명과 대소문자 관계 없이 일치")
        void shouldThrowDuplicateUsernameException_WhenUsernameCaseInsensitiveSame() {
            //given
            UserRegisterRequest userRegisterRequest = UserSample.getNormalUserRegisterRequest();
            userRegisterRequest.setNickname(userRegisterRequest.getNickname().toLowerCase());
            UserModifyRequest userModifyRequest = UserSample.getNormal2UserModifyRequest();
            userModifyRequest.setUsername(JsonNullable.of(userRegisterRequest.getNickname().toUpperCase()));
            User user = userJoinService.registerUser(userRegisterRequest);
            //when then
            assertThatThrownBy(() -> {
                userInfoService.modifyUserInfo(userModifyRequest, user.getId());
                //수정한 내용을 db에 즉시 반영하고 영속성 컨텍스트 초기화
                entityManager.flush();
                entityManager.clear();
            }).isInstanceOf(DuplicateUsernameException.class);
        }
    }
}