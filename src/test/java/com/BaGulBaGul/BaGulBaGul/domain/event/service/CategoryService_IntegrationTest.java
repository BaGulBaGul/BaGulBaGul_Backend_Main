
package com.BaGulBaGul.BaGulBaGul.domain.event.service;

import com.BaGulBaGul.BaGulBaGul.domain.event.Category;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.CategoryRegisterRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.dto.service.request.CategoryUpdateRequest;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.CategoryNameExistsException;
import com.BaGulBaGul.BaGulBaGul.domain.event.exception.CategoryNotFoundException;
import com.BaGulBaGul.BaGulBaGul.domain.event.repository.CategoryRepository;
import com.BaGulBaGul.BaGulBaGul.extension.AllTestContainerExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(AllTestContainerExtension.class)
@SpringBootTest
@Transactional
class CategoryService_IntegrationTest {

    @Autowired
    CategoryService categoryService;
    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void clear() {
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("모든 카테고리 조회 테스트")
    void getAllCategoriesTest() {
        //given
        Category category1 = categoryRepository.save(new Category("카테고리1"));
        Category category2 = categoryRepository.save(new Category("카테고리2"));

        //when
        List<Category> categories = categoryService.getAllCategories();

        //then
        assertThat(categories).extracting(Category::getName).containsExactly("카테고리1", "카테고리2");
    }

    @Test
    @DisplayName("카테고리 추가 성공 테스트")
    void addCategorySuccessTest() {
        //given
        CategoryRegisterRequest request = new CategoryRegisterRequest("새로운 카테고리");

        //when
        Long newCategoryId = categoryService.addCategory(request);

        //then
        Category newCategory = categoryRepository.findById(newCategoryId).orElse(null);
        assertThat(newCategory).isNotNull();
        assertThat(newCategory.getName()).isEqualTo("새로운 카테고리");
    }

    @Test
    @DisplayName("이미 존재하는 이름으로 카테고리 추가시 예외 발생 테스트")
    void addCategoryDuplicateNameTest() {
        //given
        categoryRepository.save(new Category("존재하는 카테고리"));
        CategoryRegisterRequest request = new CategoryRegisterRequest("존재하는 카테고리");

        //when
        //then
        assertThrows(CategoryNameExistsException.class, () -> categoryService.addCategory(request));
    }

    @Test
    @DisplayName("카테고리 수정 성공 테스트")
    void updateCategorySuccessTest() {
        //given
        Category category = categoryRepository.save(new Category("수정 전"));
        CategoryUpdateRequest request = new CategoryUpdateRequest("수정 후");

        //when
        categoryService.updateCategory(category.getId(), request);

        //then
        Category updatedCategory = categoryRepository.findById(category.getId()).orElse(null);
        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getName()).isEqualTo("수정 후");
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 수정시 예외 발생 테스트")
    void updateNotExistCategoryTest() {
        //given
        CategoryUpdateRequest request = new CategoryUpdateRequest("수정");

        //when
        //then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(999L, request));
    }

    @Test
    @DisplayName("이미 존재하는 이름으로 카테고리 수정시 예외 발생 테스트")
    void updateCategoryDuplicateNameTest() {
        //given
        categoryRepository.save(new Category("이미 있는 이름"));
        Category categoryToUpdate = categoryRepository.save(new Category("수정할 카테고리"));
        CategoryUpdateRequest request = new CategoryUpdateRequest("이미 있는 이름");

        //when
        //then
        assertThrows(CategoryNameExistsException.class, () -> categoryService.updateCategory(categoryToUpdate.getId(), request));
    }

    @Test
    @DisplayName("카테고리 삭제 성공 테스트")
    void deleteCategorySuccessTest() {
        //given
        Category category = categoryRepository.save(new Category("삭제할 카테고리"));

        //when
        categoryService.deleteCategory(category.getId());

        //then
        assertThat(categoryRepository.findById(category.getId())).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 카테고리 삭제시 예외 발생 테스트")
    void deleteNotExistCategoryTest() {
        //when
        //then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(999L));
    }
}
