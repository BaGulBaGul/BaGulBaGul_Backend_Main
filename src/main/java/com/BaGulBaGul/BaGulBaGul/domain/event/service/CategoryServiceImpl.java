package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Category;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.CategoryRepository;
import com.BaGulBaGul.BaGulBaGul.global.exception.GeneralException;
import com.BaGulBaGul.BaGulBaGul.global.response.ResponseCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.CategoryRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.CategoryUpdateRequest;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final String UK_CATEGORY_NAME = "UK__CATEGORY__NAME";

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Long addCategory(CategoryRegisterRequest categoryRegisterRequest) {
        Category category = new Category(categoryRegisterRequest.getName());
        try {
            categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            if(e.getMessage().toUpperCase().contains(UK_CATEGORY_NAME))
                throw new GeneralException(ResponseCode.EVENT_CATEGORY_EXISTS);
            throw e;
        }
        return category.getId();
    }

    @Override
    @Transactional
    public void updateCategory(Long id, CategoryUpdateRequest categoryUpdateRequest) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.EVENT_CATEGORY_NOT_EXIST));
        category.setName(categoryUpdateRequest.getName());
        try {
            categoryRepository.flush();
        } catch (DataIntegrityViolationException e) {
            if(e.getMessage().toUpperCase().contains(UK_CATEGORY_NAME))
                throw new GeneralException(ResponseCode.EVENT_CATEGORY_EXISTS);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.EVENT_CATEGORY_NOT_EXIST));
        categoryRepository.delete(category);
    }
}