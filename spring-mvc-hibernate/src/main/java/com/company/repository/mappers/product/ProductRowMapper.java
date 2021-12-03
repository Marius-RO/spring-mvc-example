package com.company.repository.mappers.product;

import com.company.model.Product;
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
public class ProductRowMapper extends AbstractMapper implements RowMapper<Product> {

    @Autowired
    public ProductRowMapper(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    public Product mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Product product = webApplicationContext.getBean(Product.class);
        product.setId(resultSet.getInt(1));
        product.setPrice(resultSet.getFloat(2));
        product.setStock(resultSet.getInt(3));
        product.setName(resultSet.getString(4));
        product.setDescription(resultSet.getString(5));
        product.setImageBase64(resultSet.getString(6));
        product.setAdded(resultSet.getTimestamp(7));
        return product;
    }
}
