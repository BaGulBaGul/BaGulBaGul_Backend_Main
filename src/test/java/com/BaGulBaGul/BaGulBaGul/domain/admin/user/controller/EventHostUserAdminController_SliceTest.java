package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AMEHUserRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AMEHUserUpdateApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.service.response.UserSearchByAdminResponse;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.service.UserAdminService;
import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManageEventHostUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManageEventHostUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserInfoService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EventHostUserAdminController_SliceTest {

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

    Long amehUserId = 1L;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(get("/").with(csrf()))
                .build();
    }

    @Test
    @DisplayName("인증 없이 이벤트 호스트 유저 검색 요청 - 401 응답")
    void searchAdminManageEventHostUser_noAuth() throws Exception {
        mockMvc.perform(get("/api/admin/user/amehuser/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("권한 없이 이벤트 호스트 유저 검색 요청 - 403 응답")
    void searchAdminManageEventHostUser_noPermission() throws Exception {
        mockMvc.perform(get("/api/admin/user/amehuser/"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_USER")
    @DisplayName("이벤트 호스트 유저 검색 요청 - 성공")
    void searchAdminManageEventHostUser_success() throws Exception {
        //given
        UserSearchByAdminResponse response = UserSearchByAdminResponse.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("testUser")
                .build();
        Page<UserSearchByAdminResponse> page = new PageImpl<>(Collections.singletonList(response), PageRequest.of(0,10), 1);

        when(userAdminService.getUserPageByAdminSearch(any(UserSearchRequest.class), any(PageRequest.class))).thenReturn(page);

        //when
        ResultActions result = mockMvc.perform(get("/api/admin/user/amehuser/")
                .param("userName", "testUser")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].userId").value(1L))
                .andExpect(jsonPath("$.data.content[0].username").value("testUser"))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("인증 없이 이벤트 호스트 유저 등록 요청 - 401 응답")
    void registerAdminManageEventHostUser_noAuth() throws Exception {
        mockMvc.perform(post("/api/admin/user/amehuser/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("권한 없이 이벤트 호스트 유저 등록 요청 - 403 응답")
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
    @DisplayName("이벤트 호스트 유저 등록 요청 - 성공")
    void registerAdminManageEventHostUser_success() throws Exception {
        //given
        AdminManageEventHostUser amehUser = Mockito.mock(AdminManageEventHostUser.class);
        when(amehUser.getId()).thenReturn(amehUserId);
        when(userJoinService.joinAdminManageEventHostUser(any(AdminManageEventHostUserJoinRequest.class))).thenReturn(amehUser);

        //when
        AMEHUserRegisterApiRequest request = AMEHUserRegisterApiRequest.builder()
                .nickname(UserSample.NORMAL_USERNAME)
                .email(UserSample.NORMAL_EMAIL)
                .build();
        ResultActions result = mockMvc.perform(post("/api/admin/user/amehuser/")                        .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(amehUserId))
                .andDo(print());
    }

    @Test
    @DisplayName("인증 없이 이벤트 호스트 유저 수정 요청 - 401 응답")
    void modifyAdminManageEventHostUser_noAuth() throws Exception {
        mockMvc.perform(patch("/api/admin/user/amehuser/{amehUserId}", amehUserId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("권한 없이 이벤트 호스트 유저 수정 요청 - 403 응답")
    void modifyAdminManageEventHostUser_noPermission() throws Exception {
        AMEHUserUpdateApiRequest request = AMEHUserUpdateApiRequest.builder()
                .username(JsonNullable.of(UserSample.NORMAL_USERNAME2))
                .email(JsonNullable.of(UserSample.NORMAL_EMAIL2))
                .build();

        mockMvc.perform(patch("/api/admin/user/amehuser/{amehUserId}", amehUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_USER")
    @DisplayName("이벤트 호스트 유저 수정 요청 - 성공")
    void modifyAdminManageEventHostUser_success() throws Exception {
        //given
        doNothing().when(userInfoService).modifyAdminManageEventHostUser(any(), any());

        //when
        AMEHUserUpdateApiRequest request = AMEHUserUpdateApiRequest.builder()
                .username(JsonNullable.of(UserSample.NORMAL_USERNAME2))
                .email(JsonNullable.of(UserSample.NORMAL_EMAIL2))
                .build();

        ResultActions result = mockMvc.perform(patch("/api/admin/user/amehuser/{amehUserId}", amehUserId)
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
    @DisplayName("인증 없이 이벤트 호스트 유저 삭제 요청 - 401 응답")
    void deleteAdminManageEventHostUser_noAuth() throws Exception {
        mockMvc.perform(delete("/api/admin/user/amehuser/{amehUserId}", amehUserId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("권한 없이 이벤트 호스트 유저 삭제 요청 - 403 응답")
    void deleteAdminManageEventHostUser_noPermission() throws Exception {
        mockMvc.perform(delete("/api/admin/user/amehuser/{amehUserId}", amehUserId))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_USER")
    @DisplayName("이벤트 호스트 유저 삭제 요청 - 성공")
    void deleteAdminManageEventHostUser_success() throws Exception {
        //given
        doNothing().when(userJoinService).deleteAdminManageEventHostUser(amehUserId);

        //when
        ResultActions result = mockMvc.perform(delete("/api/admin/user/amehuser/{amehUserId}", amehUserId));
        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }
}
