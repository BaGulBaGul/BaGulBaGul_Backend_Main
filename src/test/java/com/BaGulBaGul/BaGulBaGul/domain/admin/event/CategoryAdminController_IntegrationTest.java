package com.BaGulBaGul.BaGulBaGul.domain.admin.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.event.dto.api.request.CategoryRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.event.dto.api.request.CategoryUpdateApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.Category;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.CategoryRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.CategoryRepository;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.CategoryService;
import com.BaGulBaGul.BaGulBaGul.domain.user.User;
import com.BaGulBaGul.BaGulBaGul.domain.user.repository.UserRepository;
import com.BaGulBaGul.BaGulBaGul.domain.user.sampledata.UserSample;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserJoinService;
import com.BaGulBaGul.BaGulBaGul.domain.user.service.UserRoleService;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import com.BaGulBaGul.BaGulBaGul.global.auth.constant.GeneralRoleType;
import com.BaGulBaGul.BaGulBaGul.global.auth.service.JwtProvider;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;
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
class CategoryAdminController_IntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserJoinService userJoinService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    WebApplicationContext webApplicationContext;

    @Value("${user.login.access_token_cookie_name}")
    private String ACCESS_TOKEN_COOKIE_NAME;

    ObjectMapper objectMapper = new ObjectMapper();
    private final String TEST_CATEGORY_NAME = "test_category";

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .defaultRequest(get("/").with(csrf()))
                .build();
    }


    private String getAdminToken() {
        User admin = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        userRoleService.addRole(admin.getId(), GeneralRoleType.ADMIN.name());
        return jwtProvider.createAccessToken(admin.getId()).getJwt();
    }

    private String getNormalUserToken() {
        User user = userJoinService.registerUser(UserSample.getNormalUserRegisterRequest());
        return jwtProvider.createAccessToken(user.getId()).getJwt();
    }

    @Test
    @DisplayName("카테고리 등록 성공")
    @Transactional
    void registerCategory_success() throws Exception{
        String adminToken = getAdminToken();
        CategoryRegisterApiRequest request = new CategoryRegisterApiRequest(TEST_CATEGORY_NAME);

        mockMvc.perform(post("/api/admin/event/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNumber());

        Category category = categoryRepository.findByName(TEST_CATEGORY_NAME).orElse(null);
        assertThat(category).isNotNull();
    }

    @Test
    @DisplayName("카테고리 등록 실패 - 중복 이름")
    @Transactional
    void registerCategory_duplicate_fail() throws Exception{
        String adminToken = getAdminToken();
        categoryService.addCategory(new CategoryRegisterRequest(TEST_CATEGORY_NAME));
        CategoryRegisterApiRequest request = new CategoryRegisterApiRequest(TEST_CATEGORY_NAME);

        mockMvc.perform(post("/api/admin/event/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ResponseCode.EVENT_CATEGORY_EXISTS.getCode()))
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리 등록 실패 - 권한 없음")
    @Transactional
    void registerCategory_no_permission_fail() throws Exception{
        String normalUserToken = getNormalUserToken();
        CategoryRegisterApiRequest request = new CategoryRegisterApiRequest(TEST_CATEGORY_NAME);

        mockMvc.perform(post("/api/admin/event/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, normalUserToken))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("카테고리 수정 성공")
    @Transactional
    void updateCategory_success() throws Exception{
        String adminToken = getAdminToken();
        Long categoryId = categoryService.addCategory(new CategoryRegisterRequest(TEST_CATEGORY_NAME));
        CategoryUpdateApiRequest request = new CategoryUpdateApiRequest(TEST_CATEGORY_NAME);

        mockMvc.perform(put("/api/admin/event/category/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken))
        )
                .andExpect(status().isOk());

        Category updatedCategory = categoryRepository.findById(categoryId).orElse(null);
        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getName()).isEqualTo(TEST_CATEGORY_NAME);
    }

    @Test
    @DisplayName("카테고리 삭제 성공")
    @Transactional
    void deleteCategory_success() throws Exception{
        String adminToken = getAdminToken();
        Long categoryId = categoryService.addCategory(new CategoryRegisterRequest(TEST_CATEGORY_NAME));

        mockMvc.perform(delete("/api/admin/event/category/{id}", categoryId)
                .cookie(new Cookie(ACCESS_TOKEN_COOKIE_NAME, adminToken))
        )
                .andExpect(status().isOk());

        assertThat(categoryRepository.findById(categoryId)).isEmpty();
    }
}
