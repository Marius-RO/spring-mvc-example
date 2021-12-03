package com.company.service.impl;

import com.company.dto.CartProductDto;
import com.company.dto.OrderDetailsDto;
import com.company.model.Order;
import com.company.repository.OrderRepository;
import com.company.service.OrderService;
import com.company.service.impl.util.AbstractService;
import com.company.util.SessionCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.util.*;

@Service
public class OrderServiceImpl extends AbstractService implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(WebApplicationContext webApplicationContext, OrderRepository orderRepository) {
        super(webApplicationContext);
        this.orderRepository = orderRepository;
    }

    @Override
    protected Class<?> getClassType() {
        return OrderServiceImpl.class;
    }

    @Override
    public List<Order> getAllOrders() {
        return extractProductCartDto(orderRepository.getAllOrders());
    }

    @Override
    public List<Order> getOrdersByUserEmail(String email) {
        return extractProductCartDto(orderRepository.getOrdersByUserEmail(email));
    }

    @Override
    public void insertOrder(OrderDetailsDto orderDetailsDto, SessionCart sessionCart, String email) {
        Order order = webApplicationContext.getBean(Order.class);
        order.setAmount(sessionCart.getTotalAmount());
        order.setAdded(webApplicationContext.getBean(Timestamp.class));
        order.setUserEmail(email);
        order.setFullName(orderDetailsDto.getFullName());
        order.setFullAddress(orderDetailsDto.getFullAddress());
        order.setProductsJson(CartProductDto.fromHashSetToJson(sessionCart.getCart()));

        HashMap<Integer, Integer> quantitiesForUpdate = new HashMap<>();
        sessionCart.getCart().forEach(pr -> quantitiesForUpdate.put(pr.getProductId(), pr.getQuantity()));

        orderRepository.insertOrder(order, quantitiesForUpdate);
    }

    private List<Order> extractProductCartDto(List<Order> orders){
        orders.forEach(ord -> ord.setProductsList(CartProductDto.fromJsonToHashSet(ord.getProductsJson())));
        return orders;
    }
}
