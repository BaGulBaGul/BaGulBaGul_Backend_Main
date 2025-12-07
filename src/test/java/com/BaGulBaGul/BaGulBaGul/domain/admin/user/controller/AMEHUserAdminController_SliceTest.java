package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AMEHUserRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AMEHUserUpdateApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.service.UserAdminService;
import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManageEventHostUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManageEventHostUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserInfoService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AMEHUserAdminController_SliceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserAdminService userAdminService;

    @MockBean
    UserJoinService userJoinService;

    @MockBean
    UserInfoService userInfoService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    WebApplicationContext webApplicationContext;

    Long userId = 1L;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(get("/").with(csrf()))
                .build();
    }

    @Test
    @DisplayName("인증 없이 관리자 관리 이벤트 호스트 유저 검색 요청 - 401 응답")
    void searchAdminManageEventHostUser_noAuth() throws Exception {
        mockMvc.perform(get("/api/admin/user/amehuser/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("인증 없이 관리자 관리 이벤트 호스트 유저 등록 요청 - 401 응답")
    void registerAdminManageEventHostUser_noAuth() throws Exception {
        mockMvc.perform(post("/api/admin/user/amehuser/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("권한 없이 관리자 관리 이벤트 호스트 유저 등록 요청 - 403 응답")
    void registerAdminManageEventHostUser_noPermission() throws Exception {
        AMEHUserRegisterApiRequest request = AMEHUserRegisterApiRequest.builder()
                .nickname(UserSample.NORMAL_USERNAME)
                .email(UserSample.NORMAL_EMAIL)
                .build();
        mockMvc.perform(post("/api/admin/user/amehuser/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_USER")
    @DisplayName("관리자 관리 이벤트 호스트 유저 등록 요청 - 성공")
    void registerAdminManageEventHostUser_success() throws Exception {
        //given
        AdminManageEventHostUser amehUser = Mockito.mock(AdminManageEventHostUser.class);
        when(userAdminService.registerAMEHUserAndGetUserId(any())).thenReturn(userId);
        //when
        AMEHUserRegisterApiRequest request = AMEHUserRegisterApiRequest.builder()
                .nickname(UserSample.NORMAL_USERNAME)
                .email(UserSample.NORMAL_EMAIL)
                .build();
        ResultActions result = mockMvc.perform(post("/api/admin/user/amehuser/")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andDo(print());
    }

    @Test
    @DisplayName("인증 없이 관리자 관리 이벤트 호스트 유저 수정 요청 - 401 응답")
    void modifyAdminManageEventHostUser_noAuth() throws Exception {
        mockMvc.perform(patch("/api/admin/user/amehuser/{userId}", userId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("권한 없이 관리자 관리 이벤트 호스트 유저 수정 요청 - 403 응답")
    void modifyAdminManageEventHostUser_noPermission() throws Exception {
        AMEHUserUpdateApiRequest request = AMEHUserUpdateApiRequest.builder()
                .username(JsonNullable.of(UserSample.NORMAL_USERNAME2))
                .email(JsonNullable.of(UserSample.NORMAL_EMAIL2))
                .build();

        mockMvc.perform(patch("/api/admin/user/amehuser/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_USER")
    @DisplayName("관리자 관리 이벤트 호스트 유저 수정 요청 - 성공")
    void modifyAdminManageEventHostUser_success() throws Exception {
        //given
        doNothing().when(userInfoService).modifyAdminManageEventHostUser(any(), any());

        //when
        AMEHUserUpdateApiRequest request = AMEHUserUpdateApiRequest.builder()
                .username(JsonNullable.of(UserSample.NORMAL_USERNAME2))
                .email(JsonNullable.of(UserSample.NORMAL_EMAIL2))
                .build();

        ResultActions result = mockMvc.perform(patch("/api/admin/user/amehuser/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );
        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }

    @Test
    @DisplayName("인증 없이 관리자 관리 이벤트 호스트 유저 삭제 요청 - 401 응답")
    void deleteAdminManageEventHostUser_noAuth() throws Exception {
        mockMvc.perform(delete("/api/admin/user/amehuser/{userId}", userId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("권한 없이 관리자 관리 이벤트 호스트 유저 삭제 요청 - 403 응답")
    void deleteAdminManageEventHostUser_noPermission() throws Exception {
        mockMvc.perform(delete("/api/admin/user/amehuser/{userId}", userId))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_USER")
    @DisplayName("관리자 관리 이벤트 호스트 유저 삭제 요청 - 성공")
    void deleteAdminManageEventHostUser_success() throws Exception {
        //given when
        ResultActions result = mockMvc.perform(delete("/api/admin/user/amehuser/{userId}", userId));
        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }
}
