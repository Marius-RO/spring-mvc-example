package com.company.repository.impl;

import com.company.model.Category;
import com.company.repository.CategoryRepository;
import com.company.repository.impl.util.AbstractRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

@Repository
public class CategoryRepositoryImpl extends AbstractRepository implements CategoryRepository {

    private static final String DEF_EXTRACT_ALL_CATEGORIES_HQL = "SELECT c FROM Category c";

    @Autowired
    public CategoryRepositoryImpl(WebApplicationContext webApplicationContext, LocalSessionFactoryBean sessionFactory) {
        super(webApplicationContext, sessionFactory);
    }

    @Override
    protected Class<?> getClassType() {
        return CategoryRepositoryImpl.class;
    }

    @Override
    public List<Category> getAllCategories() {
        return super.executeFetchOperation(this::executeGetAllCategories);
    }

    private List<Category> executeGetAllCategories(Session session) {
        return session
                .createQuery(DEF_EXTRACT_ALL_CATEGORIES_HQL, Category.class)
                .list();
    }

    @Override
    public void insertCategory(Category category) {
        super.executeInsertOperation(session -> session.save(category));
    }

    @Override
    public Category getCategoryById(int categoryId) {
        return super.executeFetchOperation(session -> session.get(Category.class, categoryId));
    }

    @Override
    public void updateCategory(Category category) {
        super.executeUpdateOperation(session -> session.update(category));
    }

    @Override
    public void deleteCategoryById(int categoryId) {
        super.executeDeleteOperation(session -> executeDeleteCategoryById(session, categoryId));
    }

    private void executeDeleteCategoryById(Session session, int categoryId){
        Category category = session.get(Category.class, categoryId);
        if(category != null){
            session.remove(category);
        }
    }
}
