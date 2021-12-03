package com.company.repository.impl;

import com.company.model.Order;
import com.company.repository.OrderRepository;
import com.company.repository.impl.util.AbstractRepository;
import com.company.repository.mappers.orders.OrdersResultSetExtractor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends AbstractRepository implements OrderRepository {

    private static final String DEF_GET_ALL_ORDERS_SQL = "SELECT * FROM orders";
    private static final String DEF_GET_ORDERS_BY_EMAIL_SQL = "SELECT * FROM orders WHERE username = ?";
    private static final String DEF_INSERT_ORDER_SQL = "INSERT INTO orders (added, amount, username, full_name, full_address, products_json) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String DEF_DECREASE_PRODUCT_STOCK_SQL = "UPDATE products SET stock = stock - ? WHERE id = ?";

    public OrderRepositoryImpl(WebApplicationContext webApplicationContext, JdbcTemplate jdbcTemplate) {
        super(webApplicationContext, jdbcTemplate);
    }

    @Override
    protected Class<?> getClassType() {
        return OrderRepositoryImpl.class;
    }

    @Override
    public List<Order> getAllOrders() {
        return jdbcTemplate.query(DEF_GET_ALL_ORDERS_SQL, webApplicationContext.getBean(OrdersResultSetExtractor.class));
    }

    @Override
    public List<Order> getOrdersByUserEmail(String email) {
        return jdbcTemplate.query(DEF_GET_ORDERS_BY_EMAIL_SQL, new Object[]{email}, webApplicationContext.getBean(OrdersResultSetExtractor.class));
    }

    @Override
    @Transactional
    public void insertOrder(Order order, HashMap<Integer, Integer> quantitiesForUpdate) {
        // decrease quantities for products stock
        List<Object[]> batchArgs = new ArrayList<>();
        for(Integer productId : quantitiesForUpdate.keySet()){
            int decrement = quantitiesForUpdate.get(productId);
            batchArgs.add(new Object[]{decrement, productId});
        }

        jdbcTemplate.batchUpdate(DEF_DECREASE_PRODUCT_STOCK_SQL, batchArgs);

        // add order
        Object[] args = {order.getAdded(), order.getAmount(), order.getUserEmail(), order.getFullName(), order.getFullAddress(), order.getProductsJson()};
        jdbcTemplate.update(DEF_INSERT_ORDER_SQL, args);
    }
}
