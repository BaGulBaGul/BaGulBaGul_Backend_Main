package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Category;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.CategoryRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.CategoryUpdateRequest;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Long addCategory(CategoryRegisterRequest categoryRegisterRequest);
    void updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest);
    void deleteCategory(Long id);
}
