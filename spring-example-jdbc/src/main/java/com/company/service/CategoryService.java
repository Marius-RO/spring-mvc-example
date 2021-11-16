package com.company.service;

import com.company.dto.CategoryDto;
import com.company.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void addCategory(CategoryDto categoryDto);
    Category getCategoryById(int categoryId);
    void updateCategoryById(CategoryDto categoryDto, int categoryId);
    void deleteCategoryById(int categoryId);
}
