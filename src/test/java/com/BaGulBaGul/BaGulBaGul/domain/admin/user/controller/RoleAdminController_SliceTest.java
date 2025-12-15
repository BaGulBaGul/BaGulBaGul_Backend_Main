package com.BaGulBaGul.BaGulBaGul.domain.admin.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.AddRolePermissionsApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.DeleteRolePermissionsApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.user.dto.api.request.RoleRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.domain.user.Role;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.PermissionType;
import com.BaGulBaGul.BaGulBaGul.domain.user.dto.service.response.SearchRoleResponse;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.PermissionService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RoleAdminController_SliceTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RoleService roleService;

    @MockBean
    PermissionService permissionService;

    @Autowired
    ObjectMapper objectMapper;

    String testRoleName;
    List<PermissionType> testRolePermissions;
    List<String> testRolePermissionNames;
    Role testRole;

    @BeforeEach
    void init() {
        testRoleName = "testRole";
        testRolePermissions = List.of(PermissionType.READ_ROLE, PermissionType.MANAGE_USER);
        testRolePermissionNames = testRolePermissions.stream().map(PermissionType::name)
                .collect(Collectors.toList());
        testRole = Role.builder()
                .name(testRoleName)
                .build();
    }


    @ParameterizedTest
    @ValueSource(strings = {"MANAGE_ROLE", "READ_ROLE"})
    @DisplayName("역할 검색 요청 - 성공")
    void roleSearchTest_Success(String authority) throws Exception {
        //given when
        UserRequestPostProcessor userProcessor = user("testUser")
                .authorities(new SimpleGrantedAuthority(authority));
        ResultActions result = doSearchRole(userProcessor);
        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data.content[0].roleName").value(testRoleName))
                .andExpect(jsonPath("$.data.content[0].permissions").value(testRolePermissionNames))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andDo(print());
    }

    @Test
    @DisplayName("역할 검색 요청 - 권한 부족")
    void roleSearchTest_NoPermission() throws Exception {
        UserRequestPostProcessor userProcessor = user("testUser")
                .authorities(new SimpleGrantedAuthority("ROLE_USER"));
        ResultActions result = doSearchRole(userProcessor);
        //then
        result.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_ROLE")
    @DisplayName("역할 생성 요청 - 성공")
    void createRoleTest_Success() throws Exception {
        //given
        ResultActions result = doCreateRole();
        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("역할 생성 요청 - 권한 부족")
    void createRoleTest_NoPermission() throws Exception {
        ResultActions result = doCreateRole();
        //then
        result.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_ROLE")
    @DisplayName("역할 삭제 요청 - 성공")
    void deleteRoleTest_Success() throws Exception {
        //given
        ResultActions result = doDeleteRole();
        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("역할 삭제 요청 - 권한 부족")
    void deleteRoleTest_NoPermission() throws Exception {
        ResultActions result = doDeleteRole();
        //then
        result.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_ROLE")
    @DisplayName("역할에 권한을 추가 - 성공")
    void addPermissionsTest_Success() throws Exception {
        //given
        ResultActions result = doAddPermission();
        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("역할에 권한을 추가 - 권한 부족")
    void addPermissionsTest_NoPermission() throws Exception {
        ResultActions result = doAddPermission();
        //then
        result.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "MANAGE_ROLE")
    @DisplayName("역할에 권한을 삭제 - 성공")
    void deletePermissionsTest_Success() throws Exception {
        //given
        ResultActions result = doDeletePermission();
        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("역할에 권한을 삭제 - 권한 부족")
    void deletePermissionsTest_NoPermission() throws Exception {
        ResultActions result = doDeletePermission();
        //then
        result.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "READ_ROLE")
    @DisplayName("모든 권한을 검색 - 성공")
    void findAllPermissions_Success() throws Exception {
        //when
        ResultActions result = mockMvc.perform(get("/api/admin/user/role/permissions")
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        List<String> allPermissionNames = Arrays.stream(PermissionType.values())
                .map(PermissionType::name)
                .collect(Collectors.toList());
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data.permissions").value(allPermissionNames))
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    @DisplayName("모든 권한을 검색 - 권한 부족")
    void findAllPermissions_NoPermission() throws Exception {
        //when
        ResultActions result = mockMvc.perform(get("/api/admin/user/role/permissions")
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result.andExpect(MockMvcResultMatchers.status().isForbidden())
                .andDo(print());
    }

    private ResultActions doSearchRole(UserRequestPostProcessor userProcessor) throws Exception {
        //given
        PageImpl<SearchRoleResponse> mockResult = new PageImpl<>(
                List.of(
                        SearchRoleResponse.builder()
                                .roleName(testRoleName)
                                .permissions(testRolePermissions)
                                .build()
                )
        );
        when(roleService.findRoleByCondition(any(), any())).thenReturn(mockResult);
        //when
        ResultActions result = mockMvc.perform(get("/api/admin/user/role/")
                .with(userProcessor)
                .param("roleName", testRoleName)
                .param("permissions", testRolePermissionNames.toArray(String[]::new))
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
        );
        return result;
    }

    private ResultActions doCreateRole() throws Exception {
        //given
        when(roleService.createRole(any())).thenReturn(testRole);
        //when
        RoleRegisterApiRequest apiRequest = RoleRegisterApiRequest.builder()
                .roleName(testRoleName)
                .build();
        ResultActions result = mockMvc.perform(post("/api/admin/user/role/").with(csrf())
                .content(objectMapper.writeValueAsString(apiRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );
        return result;
    }

    private ResultActions doDeleteRole() throws Exception {
        //given
        doNothing().when(roleService).deleteRole(any());
        //when
        ResultActions result = mockMvc.perform(delete("/api/admin/user/role/{roleName}", testRoleName).with(csrf()));
        return result;
    }


    private ResultActions doAddPermission() throws Exception {
        //given
        doNothing().when(permissionService).addPermissions(any(), any());
        //when
        AddRolePermissionsApiRequest apiRequest = AddRolePermissionsApiRequest.builder()
                .permissions(testRolePermissions)
                .build();
        ResultActions result = mockMvc.perform(post("/api/admin/user/role/").with(csrf())
                .content(objectMapper.writeValueAsString(apiRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );
        return result;
    }

    private ResultActions doDeletePermission() throws Exception {
        //given
        doNothing().when(permissionService).deletePermissions(any(), any());
        //when
        DeleteRolePermissionsApiRequest apiRequest = DeleteRolePermissionsApiRequest.builder()
                .permissions(testRolePermissions)
                .build();
        ResultActions result = mockMvc.perform(delete("/api/admin/user/role/{roleName}", testRoleName).with(csrf())
                .content(objectMapper.writeValueAsString(apiRequest))
                .contentType(MediaType.APPLICATION_JSON)
        );
        return result;
    }
}
