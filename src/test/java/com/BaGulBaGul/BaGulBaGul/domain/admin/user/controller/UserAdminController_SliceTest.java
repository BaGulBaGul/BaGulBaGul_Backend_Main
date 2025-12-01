package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.service.response.UserSearchByAdminResponse;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.service.UserAdminService;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.request.UserSearchRequest;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.util.Collections;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserAdminController_SliceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserAdminService userAdminService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("인증 없이 유저 검색 요청 - 401 응답")
    void searchUser_noAuth() throws Exception {
        mockMvc.perform(get("/api/admin/user/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("권한 없이 유저 검색 요청 - 403 응답")
    void searchUser_noPermission() throws Exception {
        mockMvc.perform(get("/api/admin/user/"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_USER")
    @DisplayName("유저 검색 요청 - 성공")
    void searchUser_success() throws Exception {
        //given
        UserSearchByAdminResponse response = UserSearchByAdminResponse.builder()
                .id(1L)
                .email("test@test.com")
                .nickname("testUser")
                .build();
        Page<UserSearchByAdminResponse> page = new PageImpl<>(Collections.singletonList(response), PageRequest.of(0,10), 1);

        when(userAdminService.getUserPageByAdminSearch(any(UserSearchRequest.class), any(PageRequest.class))).thenReturn(page);

        //when
        ResultActions result = mockMvc.perform(get("/api/admin/user/")
                .param("userName", "testUser")
                .param("roles", "USER")
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
}
