package com.BaGulBaGul.BaGulBaGul.domain.admin.report.controller;

import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.EventRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.EventService;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportContentType;
import com.BaGulBaGul.BaGulBaGul.domain.report.constant.ReportStatusState;
import com.BaGulBaGul.BaGulBaGul.domain.report.Report;
import com.BaGulBaGul.BaGulBaGul.domain.report.service.ReportService;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.dto.AuthenticatedUserInfo;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import com.BaGulBaGul.BaGulBaGul.domain.report.repository.ReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.stream.Collectors;

import static com.BaGulBaGul.BaGulBaGul.domain.event.sampledata.EventSample.getNormalRegisterRequest;
import static com.BaGulBaGul.BaGulBaGul.domain.report.sampledata.ReportSample.getNormalReportRegisterRequest;
import static com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample.getAdminUserRegisterRequest;
import static com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample.getNormalUserRegisterRequest;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("관리자 신고 상태 컨트롤러 통합 테스트")
class ReportStatusControllerImpl_IntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserJoinService userJoinService;
    @Autowired
    EventService eventService;
    @Autowired
    ReportService reportService;
    @Autowired
    ReportRepository reportRepository;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Value("${user.login.access_token_cookie_name}")
    private String ACCESS_TOKEN_COOKIE_NAME;

    User admin;
    User user;
    String adminToken;
    String userToken;
    AuthenticatedUserInfo adminAuth;
    AuthenticatedUserInfo userAuth;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(post("/").with(csrf()))
                .build();

        admin = userJoinService.registerUser(getAdminUserRegisterRequest());
        user = userJoinService.registerUser(getNormalUserRegisterRequest());
        adminToken = jwtProvider.createAccessToken(admin.getId()).getJwt();
        userToken = jwtProvider.createAccessToken(user.getId()).getJwt();

        adminAuth = AuthenticatedUserInfo.builder()
                .userId(admin.getId())
                .roles(List.of(GeneralRoleType.ADMIN.name(), GeneralRoleType.EVENT_HOST.name()))
                .build();
        userAuth = AuthenticatedUserInfo.builder()
                .userId(user.getId())
                .roles(List.of(GeneralRoleType.USER.name()))
                .build();
    }

    @Test
    @DisplayName("인증 없이 접근")
    void findReportStatusByConditionAndPageable_unauthorized() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/admin/report-status/")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("일반 유저로 접근")
    void findReportStatusByConditionAndPageable_forbidden() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/admin/report-status/")
                        .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, userToken))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("관리자로 신고 상태 조회 성공")
    void findReportStatusByConditionAndPageable_success() throws Exception {
        //given
        EventRegisterRequest eventRegisterRequest = getNormalRegisterRequest(user.getId());
        Long eventId = eventService.registerEvent(adminAuth, eventRegisterRequest);
        reportService.registerEventReport(eventId, getNormalReportRegisterRequest(user.getId()));

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/admin/report-status/")
                        .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content[0].reportStatusInfo").exists())
                .andExpect(jsonPath("$.data.content[0].reportStatusInfo.reportStatusId").isNumber())
                .andExpect(jsonPath("$.data.content[0].reportStatusInfo.state").value(ReportStatusState.PROCEEDING.name()))
                .andExpect(jsonPath("$.data.content[0].eventInfo").exists())
                .andExpect(jsonPath("$.data.content[0].eventInfo.event.eventId").value(eventId))
                .andExpect(jsonPath("$.errorCode").value(ResponseCode.SUCCESS.getCode()))
                .andDo(print());
    }

    @Test
    @DisplayName("관리자로 신고 상태 조회 성공 - 컨텐츠 타입 필터링")
    void findReportStatusByConditionAndPageable_withContentTypeFilter() throws Exception {
        //given
        EventRegisterRequest eventRegisterRequest = getNormalRegisterRequest(user.getId());
        Long eventId = eventService.registerEvent(adminAuth, eventRegisterRequest);
        reportService.registerEventReport(eventId, getNormalReportRegisterRequest(user.getId()));

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/admin/report-status/")
                        .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken))
                        .queryParam("reportContentTypes", ReportContentType.Event.name())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].eventInfo.event.eventId").value(eventId))
                .andExpect(jsonPath("$.errorCode").value(ResponseCode.SUCCESS.getCode()))
                .andDo(print());

        //given
        //when
        ResultActions resultActions2 = mockMvc.perform(
                get("/api/admin/report-status/")
                        .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken))
                        .queryParam("reportContentTypes", ReportContentType.Recruitment.name())
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions2.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(0))
                .andExpect(jsonPath("$.errorCode").value(ResponseCode.SUCCESS.getCode()))
                .andDo(print());
    }

    @Test
    @DisplayName("인증 없이 특정 신고 상태의 신고 조회")
    void findReportByReportStatusAndPageable_unauthorized() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/admin/report-status/1/report/")
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("일반 유저로 특정 신고 상태의 신고 조회")
    void findReportByReportStatusAndPageable_forbidden() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/admin/report-status/1/report/")
                        .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, userToken))
                        .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        resultActions.andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @DisplayName("관리자로 특정 신고 상태의 신고 조회")
    void findReportByReportStatusAndPageable_success() throws Exception {
        //given
        EventRegisterRequest eventRegisterRequest = getNormalRegisterRequest(user.getId());
        Long eventId = eventService.registerEvent(adminAuth, eventRegisterRequest);
        Long reportId = reportService.registerEventReport(eventId, getNormalReportRegisterRequest(user.getId()));
        Report report = reportRepository.findById(reportId).get();
        Long reportStatusId = report.getReportStatus().getId();

        //when
        ResultActions resultActions = mockMvc.perform(
                get("/api/admin/report-status/{reportStatusId}/report/", reportStatusId)
                        .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken))
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].reportId").value(reportId))
                .andExpect(jsonPath("$.errorCode").value(ResponseCode.SUCCESS.getCode()))
                .andDo(print());
    }
}