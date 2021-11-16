package com.company.service.impl;

import com.company.dto.CategoryDto;
import com.company.model.Category;
import com.company.repository.CategoryRepository;
import com.company.service.CategoryService;
import com.company.service.impl.util.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@Service
public class CategoryServiceImpl extends AbstractService implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(WebApplicationContext webApplicationContext, CategoryRepository categoryRepository) {
        super(webApplicationContext);
        this.categoryRepository = categoryRepository;
    }

    @Override
    protected Class<?> getClassType() {
        return CategoryServiceImpl.class;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    @Override
    public void addCategory(CategoryDto categoryDto) {
        Category category = webApplicationContext.getBean(Category.class);
        category.setName(categoryDto.getName());
        categoryRepository.insertCategory(category);
    }

    @Override
    public Category getCategoryById(int categoryId) {
        return categoryRepository.getCategoryById(categoryId);
    }

    @Override
    public void updateCategoryById(CategoryDto categoryDto, int categoryId) {
        Category category = webApplicationContext.getBean(Category.class);
        category.setId(categoryId);
        category.setName(categoryDto.getName());
        categoryRepository.updateCategory(category);
    }

    @Override
    public void deleteCategoryById(int categoryId) {
        categoryRepository.deleteCategoryById(categoryId);
    }
}
