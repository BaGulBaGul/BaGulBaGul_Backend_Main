package com.BaGulBaGul.BaGulBaGul.domain.admin.event.controller;

import com.BaGulBaGul.BaGulBaGul.domain.admin.event.dto.api.request.CategoryRegisterApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.admin.event.dto.api.request.CategoryUpdateApiRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.service.CategoryService;
import com.BaGulBaGul.BaGulBaGul.global.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/event/category")
@RequiredArgsConstructor
@Api(tags = "관리자 - 이벤트 카테고리 관리", description = "MANAGE_EVENT 권한 필요")
@PreAuthorize("hasAuthority('MANAGE_EVENT')")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping("")
    @Operation(summary = "카테고리 등록", description = "카테고리를 등록합니다.")
    public ResponseEntity<ApiResponse<Long>> registerCategory(
            @RequestBody CategoryRegisterApiRequest categoryRegisterApiRequest
    ) {
        Long id = categoryService.addCategory(categoryRegisterApiRequest.toServiceRequest());
        return ResponseEntity.ok(ApiResponse.of(id));
    }

    @PutMapping("/{eventCategoryId}")
    @Operation(summary = "카테고리 수정", description = "카테고리 이름을 수정합니다.")
    public ResponseEntity<ApiResponse<Void>> updateCategory(
            @PathVariable(name="eventCategoryId") Long eventCategoryId,
            @RequestBody CategoryUpdateApiRequest categoryUpdateApiRequest
    ) {
        categoryService.updateCategory(eventCategoryId, categoryUpdateApiRequest.toServiceRequest());
        return ResponseEntity.ok(ApiResponse.of(null));
    }

    @DeleteMapping("/{eventCategoryId}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다. 모든 이벤트에서 해당 카테고리는 지워집니다.")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @PathVariable(name="eventCategoryId") Long eventCategoryId
    ) {
        categoryService.deleteCategory(eventCategoryId);
        return ResponseEntity.ok(ApiResponse.of(null));
    }
}

