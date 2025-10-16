package com.BaGulBaGul.BaGulBaGul.domain.admin.event.controller;

import com.BaGulBaGul.BaGulBaGul.domain.event.Event;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.EventRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventAdminController_IntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserJoinService userJoinService;
    @Autowired
    EventService eventService;
    @Autowired
    EventRepository eventRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    EntityManager em;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Value("${user.login.access_token_cookie_name}")
    private String ACCESS_TOKEN_COOKIE_NAME;

    User admin;
    User eventHost;
    User user;
    String adminToken;
    String eventHostToken;
    String userToken;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(post("/").with(csrf()))
                .build();
    }

    @BeforeEach
    @Transactional
    void setUp() {
        admin = userJoinService.registerUser(UserSample.getAdminUserRegisterRequest());
        eventHost = userJoinService.registerUser(UserSample.getEventHostUserRegisterRequest());
        user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        adminToken = jwtProvider.createAccessToken(admin.getId()).getJwt();
        eventHostToken = jwtProvider.createAccessToken(eventHost.getId()).getJwt();
        userToken = jwtProvider.createAccessToken(user.getId()).getJwt();
    }

    @Nested
    @DisplayName("삭제된 이벤트 조회 테스트")
    class GetDeletedEventTest {
        @Test
        @DisplayName("관리자는 삭제된 이벤트를 조회할 수 있다")
        @Transactional
        void getDeletedEvent_asAdmin() throws Exception {
            //given
            AuthenticatedUserInfo eventHostInfo = new AuthenticatedUserInfo(eventHost.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            Long eventId = eventService.registerEvent(eventHostInfo, EventSample.getNormalRegisterRequest(eventHost.getId()));
            eventService.deleteEvent(eventHostInfo, eventId);
            em.flush();
            em.clear();

            //when
            //then
            mockMvc.perform(get("/api/admin/event/search")
                            .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken))
                            .param("deleted", "true")
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.content[0].event.eventId").value(eventId));
        }

        @Test
        @DisplayName("일반 유저는 접근할 수 없다")
        @Transactional
        void getDeletedEvent_asUser() throws Exception {
            //given
            //when
            //then
            mockMvc.perform(get("/api/admin/event/search")
                            .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, userToken))
                            .param("deleted", "true")
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("삭제된 이벤트 복구 테스트")
    class RestoreDeletedEventTest {
        @Test
        @DisplayName("관리자는 삭제된 이벤트를 복구할 수 있다")
        @Transactional
        void restoreDeletedEvent_asAdmin() throws Exception {
            //given
            AuthenticatedUserInfo eventHostInfo = new AuthenticatedUserInfo(eventHost.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            Long eventId = eventService.registerEvent(eventHostInfo, EventSample.getNormalRegisterRequest(eventHost.getId()));
            eventService.deleteEvent(eventHostInfo, eventId);
            em.flush();
            em.clear();

            //when
            //then
            mockMvc.perform(post("/api/admin/event/deleted/{eventId}/restore", eventId)
                            .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.errorCode").value(ResponseCode.SUCCESS.getCode()));

            Event event = eventRepository.findById(eventId).orElse(null);
            assertThat(event).isNotNull();
            assertThat(event.getDeleted()).isFalse();
        }

        @Test
        @DisplayName("일반 유저는 접근할 수 없다")
        @Transactional
        void restoreDeletedEvent_asUser() throws Exception {
            //given
            AuthenticatedUserInfo eventHostInfo = new AuthenticatedUserInfo(eventHost.getId(), List.of(GeneralRoleType.EVENT_HOST.name()));
            Long eventId = eventService.registerEvent(eventHostInfo, EventSample.getNormalRegisterRequest(eventHost.getId()));
            eventService.deleteEvent(eventHostInfo, eventId);
            em.flush();
            em.clear();

            //when
            //then
            mockMvc.perform(post("/api/admin/event/deleted/{eventId}/restore", eventId)
                            .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, userToken))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isForbidden());
        }
    }
}
