
package com.BaGulBaGul.BaGulBaGul.domain.admin.event.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.event.dto.api.request.EventBannerModifyApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.upload.service.ResourceService;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EventBannerAdminController_IntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserJoinService userJoinService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    EventService eventService;
    @Autowired
    ResourceService resourceService;

    @MockBean
    AmazonS3 amazonS3;

    @Value("${user.login.access_token_cookie_name}")
    private String ACCESS_TOKEN_COOKIE_NAME;

    private final ObjectMapper objectMapper = new ObjectMapper();

    User admin;
    User normalUser;
    String adminToken;
    String normalUserToken;
    List<Long> eventIds = new ArrayList<>();
    List<Long> resourceIds = new ArrayList<>();

    @BeforeEach
    void setup() throws IOException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(get("/").with(csrf()))
                .build();
        //유저 등록
        UserRegisterRequest adminRegisterRequest = UserSample.getAdminUserRegisterRequest();
        admin = userJoinService.registerUser(adminRegisterRequest);
        adminToken = jwtProvider.createAccessToken(admin.getId()).getJwt();

        UserRegisterRequest normalUserRegisterRequest = UserSample.getNormalUserRegisterRequest();
        normalUser = userJoinService.registerUser(normalUserRegisterRequest);
        normalUserToken = jwtProvider.createAccessToken(normalUser.getId()).getJwt();

        //이벤트, 리소스 등록
        AuthenticatedUserInfo authenticatedUserInfo = AuthenticatedUserInfo.builder()
                .userId(admin.getId())
                .roles(List.of(GeneralRoleType.ADMIN.name(), GeneralRoleType.EVENT_HOST.name()))
                .build();
        for(int i=0;i<5;i++) {
            EventRegisterRequest eventRegisterRequest = EventSample.getNormalRegisterRequest(admin.getId());
            eventIds.add(eventService.registerEvent(authenticatedUserInfo, eventRegisterRequest));
            resourceIds.add(resourceService.uploadResource("", new MockMultipartFile("test"+i, ("test"+i+".jpg"), "image/jpeg", "test".getBytes())));
        }
    }

    @Nested
    @DisplayName("배너 설정 테스트")
    class ModifyEventBannerTest {
        @Test
        @DisplayName("성공")
        void success() throws Exception {
            //given
            List<EventBannerModifyApiRequest> requests = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                requests.add(new EventBannerModifyApiRequest(Long.valueOf(i+1), eventIds.get(i), resourceIds.get(i)));
            }

            //when
            //then
            mockMvc.perform(patch("/api/admin/event/banner/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requests))
                    .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken))
            ).andExpect(status().isOk());
        }

        @Test
        @DisplayName("실패 - 권한 없음")
        void fail_no_auth() throws Exception {
            //given
            List<EventBannerModifyApiRequest> requests = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                requests.add(new EventBannerModifyApiRequest(Long.valueOf(i+1), eventIds.get(i), resourceIds.get(i)));
            }

            //when
            //then
            mockMvc.perform(patch("/api/admin/event/banner/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requests))
                    .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, normalUserToken))
            ).andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 이벤트")
        void fail_no_event() throws Exception {
            //given
            List<EventBannerModifyApiRequest> requests = new ArrayList<>();
            requests.add(new EventBannerModifyApiRequest(1L, 999L, resourceIds.get(0)));


            //when
            //then
            ResponseCode responseCode = ResponseCode.EVENT_NOT_FOUND;
            mockMvc.perform(patch("/api/admin/event/banner/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requests))
                    .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken))
            )
                    .andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()));
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 리소스")
        void fail_no_resource() throws Exception {
            //given
            List<EventBannerModifyApiRequest> requests = new ArrayList<>();
            requests.add(new EventBannerModifyApiRequest(1L, eventIds.get(0), 999L));


            //when
            //then
            ResponseCode responseCode = ResponseCode.UPLOAD_RESOURCE_NOT_FOUND;
            mockMvc.perform(patch("/api/admin/event/banner/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requests))
                    .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken))
            )
                    .andExpect(status().is(responseCode.getHttpStatus().value()))
                    .andExpect(jsonPath("$.errorCode").value(responseCode.getCode()));
        }
    }
}
