package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AMPWUserRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.service.UserAdminService;
import com.BaGulBaGul.BaGulBaGul.domain.user.AdminManagePasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.PasswordLoginUser;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.AdminManagePasswordLoginUserJoinRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.PasswordLoginUserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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

@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AMPWUserAdminController_SliceTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserJoinService userJoinService;

    @MockBean
    UserAdminService userAdminService;

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
    @DisplayName("인증 없이 관리자 관리 패스워드 로그인 유저 등록 요청 - 401 응답")
    void registerAdminManageEventHostUser_noAuth() throws Exception {
        mockMvc.perform(post("/api/admin/user/ampwuser/")
                        .with(csrf())
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("권한 없이 관리자 관리 패스워드 로그인 유저 등록 요청 - 403 응답")
    void registerAdminManageEventHostUser_noPermission() throws Exception {
        AMPWUserRegisterApiRequest request = AMPWUserRegisterApiRequest.builder()
                .loginId(PasswordLoginUserSample.normalLoginId)
                .loginPw(PasswordLoginUserSample.normalLoginPassword)
                .nickname(UserSample.NORMAL_USERNAME)
                .build();
        mockMvc.perform(post("/api/admin/user/ampwuser/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_USER")
    @DisplayName("관리자 관리 패스워드 로그인 등록 요청 - 성공")
    void registerAdminManageEventHostUser_success() throws Exception {
        //given
        when(userAdminService.registerAMPWUserAndGetUserId(any())).thenReturn(userId);

        //when
        AMPWUserRegisterApiRequest request = AMPWUserRegisterApiRequest.builder()
                .loginId(PasswordLoginUserSample.normalLoginId)
                .loginPw(PasswordLoginUserSample.normalLoginPassword)
                .nickname(UserSample.NORMAL_USERNAME)
                .build();
        ResultActions result = mockMvc.perform(post("/api/admin/user/ampwuser/")
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
    @DisplayName("인증 없이 관리자 관리 패스워드 로그인 유저 삭제 요청 - 401 응답")
    void deleteAdminManageEventHostUser_noAuth() throws Exception {
        mockMvc.perform(delete("/api/admin/user/ampwuser/{userId}", userId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("권한 없이 관리자 관리 패스워드 로그인 유저 삭제 요청 - 403 응답")
    void deleteAdminManageEventHostUser_noPermission() throws Exception {
        //when
        ResultActions result = mockMvc.perform(delete("/api/admin/user/ampwuser/{userId}", userId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_USER")
    @DisplayName("관리자 관리 패스워드 로그인 삭제 요청 - 성공")
    void deleteAdminManageEventHostUser_success() throws Exception {
        //given when
        ResultActions result = mockMvc.perform(delete("/api/admin/user/ampwuser/{userId}", userId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }
}
