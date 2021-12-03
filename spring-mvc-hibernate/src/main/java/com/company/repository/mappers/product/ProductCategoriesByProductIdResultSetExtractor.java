package com.company.repository.mappers.product;


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
public class ProductCategoriesByProductIdResultSetExtractor extends AbstractMapper implements ResultSetExtractor<List<Integer>> {

    @Autowired
    public ProductCategoriesByProductIdResultSetExtractor(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    public List<Integer> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Integer> ids = new ArrayList<>();

        while(resultSet.next()){
            ids.add(resultSet.getInt(1));
        }

        return ids;
    }
}
