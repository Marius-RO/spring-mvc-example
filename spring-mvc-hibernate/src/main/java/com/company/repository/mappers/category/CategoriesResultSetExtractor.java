package com.company.repository.mappers.category;

import com.company.model.Category;
import com.company.repository.mappers.AbstractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CategoriesResultSetExtractor extends AbstractMapper implements ResultSetExtractor<List<Category>> {

    @Autowired
    public CategoriesResultSetExtractor(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    public List<Category> extractData(ResultSet resultSet) throws SQLException, DataAccessException {

        List<Category> categories = new ArrayList<>();

        while(resultSet.next()){
            Category category = webApplicationContext.getBean(Category.class);
            category.setId(resultSet.getInt(1));
            category.setName(resultSet.getString(2));
            categories.add(category);
        }

        return categories;
    }
}
