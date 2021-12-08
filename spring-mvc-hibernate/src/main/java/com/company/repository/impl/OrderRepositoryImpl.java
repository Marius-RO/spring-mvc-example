package com.company.repository.impl;

import com.company.model.Order;
import com.company.model.Product;
import com.company.repository.OrderRepository;
import com.company.repository.impl.util.AbstractRepository;
import org.hibernate.Session;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends AbstractRepository implements OrderRepository {

    private static final String DEF_GET_ALL_ORDERS_HQL = "SELECT o FROM Order o";
    private static final String DEF_GET_ORDERS_BY_EMAIL_HQL = "SELECT o FROM Order o WHERE o.userEmail = ?1";

    public OrderRepositoryImpl(WebApplicationContext webApplicationContext, LocalSessionFactoryBean sessionFactory) {
        super(webApplicationContext, sessionFactory);
    }

    @Override
    protected Class<?> getClassType() {
        return OrderRepositoryImpl.class;
    }

    @Override
    public List<Order> getAllOrders() {
        return super.executeFetchOperation(this::executeGetAllOrders);
    }

    private List<Order> executeGetAllOrders(Session session){
        return session
                .createQuery(DEF_GET_ALL_ORDERS_HQL, Order.class)
                .list();
    }

    @Override
    public List<Order> getOrdersByUserEmail(String email) {
        return super.executeFetchOperation(session -> executeGetOrdersByUserEmail(session, email));
    }

    private List<Order> executeGetOrdersByUserEmail(Session session, String userEmail){
        return session
                .createQuery(DEF_GET_ORDERS_BY_EMAIL_HQL, Order.class)
                .setParameter(1, userEmail)
                .list();
    }

    @Override
    public void insertOrder(Order order, HashMap<Integer, Integer> quantitiesForUpdate) {
        super.executeUpdateOperation(session -> executeInsertOrder(session, order, quantitiesForUpdate));
    }

    private void executeInsertOrder(Session session, Order order, HashMap<Integer, Integer> quantitiesForUpdate) {
        decreaseQuantitiesForProductsStock(session, quantitiesForUpdate);
        session.save(order);
    }

    private void decreaseQuantitiesForProductsStock(Session session, HashMap<Integer, Integer> quantitiesForUpdate){
        for(Integer productId : quantitiesForUpdate.keySet()){
            int decrement = quantitiesForUpdate.get(productId);
            Product product = session.get(Product.class, productId);
            if(product != null){
                product.setStock(product.getStock() - decrement);
                session.update(product);
            }
        }
    }
}
