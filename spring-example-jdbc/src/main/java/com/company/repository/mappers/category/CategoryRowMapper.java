package com.company.repository.mappers.category;

import com.company.model.Category;
import com.company.repository.mappers.AbstractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CategoryRowMapper extends AbstractMapper implements RowMapper<Category> {

    @Autowired
    public CategoryRowMapper(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    public Category mapRow(ResultSet resultSet, int i) throws SQLException {
        Category category = webApplicationContext.getBean(Category.class);
        category.setId(resultSet.getInt(1));
        category.setName(resultSet.getString(2));
        return category;
    }
}
