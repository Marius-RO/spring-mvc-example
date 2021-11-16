package com.company.repository.impl;

import com.company.model.Category;
import com.company.repository.CategoryRepository;
import com.company.repository.impl.util.AbstractRepository;
import com.company.repository.mappers.category.CategoriesResultSetExtractor;
import com.company.repository.mappers.category.CategoryRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@Repository
public class CategoryRepositoryImpl extends AbstractRepository implements CategoryRepository {

    public static final String DEF_EXTRACT_ALL_CATEGORIES_SQL = "SELECT * FROM categories";
    public static final String DEF_INSERT_CATEGORY_SQL = "INSERT INTO categories (name) VALUES (?)";
    public static final String DEF_GET_CATEGORY_BY_ID_SQL = "SELECT * FROM categories WHERE id = ?";
    public static final String DEF_UPDATE_CATEGORY_BY_ID_SQL = "UPDATE categories SET name = ? WHERE id = ?";
    public static final String DEF_DELETE_CATEGORY_BY_ID_SQL = "DELETE FROM categories WHERE id = ?";

    @Autowired
    public CategoryRepositoryImpl(WebApplicationContext webApplicationContext, JdbcTemplate jdbcTemplate) {
        super(webApplicationContext, jdbcTemplate);
    }

    @Override
    protected Class<?> getClassType() {
        return CategoryRepositoryImpl.class;
    }

    @Override
    public List<Category> getAllCategories() {
        return jdbcTemplate.query(DEF_EXTRACT_ALL_CATEGORIES_SQL, webApplicationContext.getBean(CategoriesResultSetExtractor.class));
    }

    @Override
    public void insertCategory(Category category) {
        Object[] args = {category.getName()};
        jdbcTemplate.update(DEF_INSERT_CATEGORY_SQL, args);
    }

    @Override
    public Category getCategoryById(int categoryId) {
        try{
            Object[] args = {categoryId};
            return jdbcTemplate.queryForObject(DEF_GET_CATEGORY_BY_ID_SQL, args, webApplicationContext.getBean(CategoryRowMapper.class));
        }
        catch (DataAccessException ex){
            return webApplicationContext.getBean(Category.class);
        }
    }

    @Override
    public void updateCategory(Category category) {
        Object[] args = {category.getName(),category.getId()};
        jdbcTemplate.update(DEF_UPDATE_CATEGORY_BY_ID_SQL, args);
    }

    @Override
    public void deleteCategoryById(int categoryId) {
        Object[] args = {categoryId};
        jdbcTemplate.update(DEF_DELETE_CATEGORY_BY_ID_SQL, args);
    }
}
