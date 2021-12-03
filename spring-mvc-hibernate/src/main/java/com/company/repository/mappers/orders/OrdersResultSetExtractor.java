package com.company.repository.mappers.orders;

import com.company.model.Order;
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
public class OrdersResultSetExtractor extends AbstractMapper implements ResultSetExtractor<List<Order>> {

    @Autowired
    public OrdersResultSetExtractor(WebApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    public List<Order> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<Order> orders = new ArrayList<>();

        while(resultSet.next()){
            Order order = webApplicationContext.getBean(Order.class);
            order.setId(resultSet.getInt(1));
            order.setAdded(resultSet.getTimestamp(2));
            order.setAmount(resultSet.getDouble(3));
            order.setUserEmail(resultSet.getString(4));
            order.setFullName(resultSet.getString(5));
            order.setFullAddress(resultSet.getString(6));
            order.setProductsJson(resultSet.getString(7));

            orders.add(order);
        }

        return orders;
    }
}
