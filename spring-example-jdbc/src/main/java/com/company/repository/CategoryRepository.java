package com.company.repository;

import com.company.model.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> getAllCategories();
    void insertCategory(Category category);
    Category getCategoryById(int categoryId);
    void updateCategory(Category category);
    void deleteCategoryById(int categoryId);
}
