package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AddUserRoleApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.RemoveUserRoleApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.SetUserRoleApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserRoleService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserRoleAdminController_SliceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRoleService userRoleService;

    @Autowired
    ObjectMapper objectMapper;

    Long userId = 1L;
    
    @Test
    @WithMockUser(authorities = "MANAGE_USER")
    @DisplayName("유저 역할 변경 성공")
    void testSetUserRole_Success() throws Exception {
        ResultActions result = doSetUserRoles();
        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }


    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("유저 역할 변경 실패 - 권한 없음")
    void testSetUserRole_NoPermission() throws Exception {
        //given
        ResultActions result = doSetUserRoles();
        //then
        result.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_USER")
    @DisplayName("유저 역할 추가 성공")
    void testAddUserRole_Success() throws Exception {
        //given
        ResultActions result = doAddRoles();
        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("유저 역할 추가 실패 - 권한 없음")
    void testAddUserRole_NoPermission() throws Exception {
        ResultActions result = doAddRoles();
        //then
        result.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_USER")
    @DisplayName("유저 역할 제거 성공")
    void testDeleteUserRole_Success() throws Exception {
        //given
        ResultActions result = doDeleteUserRoles();
        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("유저 역할 제거 실패 - 권한 없음")
    void testDeleteUserRole_NoPermission() throws Exception {
        ResultActions result = doDeleteUserRoles();
        //then
        result.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }

    private ResultActions doSetUserRoles() throws Exception {
        //given
        doNothing().when(userRoleService).changeRole(any(), any());
        //when
        SetUserRoleApiRequest apiRequest = SetUserRoleApiRequest
                .builder()
                .roles(Set.of("testRole1", "testRole2"))
                .build();
        ResultActions result = mockMvc.perform(put("/api/admin/user/{userId}/roles/", userId)
                .with(csrf())
                .content(objectMapper.writeValueAsString(apiRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );

        return result;
    }

    private ResultActions doAddRoles() throws Exception {
        //given
        doNothing().when(userRoleService).addRoles(any(), any());
        //when
        AddUserRoleApiRequest apiRequest = AddUserRoleApiRequest
                .builder()
                .roles(Set.of("testRole1", "testRole2"))
                .build();
        ResultActions result = mockMvc.perform(post("/api/admin/user/{userId}/roles/", userId)
                .with(csrf())
                .content(objectMapper.writeValueAsString(apiRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );
        return result;
    }

    private ResultActions doDeleteUserRoles() throws Exception {
        //given
        doNothing().when(userRoleService).deleteRoles(any(), any());
        //when
        RemoveUserRoleApiRequest apiRequest = RemoveUserRoleApiRequest
                .builder()
                .roles(Set.of("testRole1", "testRole2"))
                .build();
        ResultActions result = mockMvc.perform(delete("/api/admin/user/{userId}/roles/", userId)
                .with(csrf())
                .content(objectMapper.writeValueAsString(apiRequest))
                .contentType(MediaType.APPLICATION_JSON)
         );
        return result;
    }
}
