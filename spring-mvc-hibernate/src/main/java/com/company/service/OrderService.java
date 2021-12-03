package com.company.service;

import com.company.dto.OrderDetailsDto;
import com.company.model.Order;
import com.company.util.SessionCart;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    List<Order> getOrdersByUserEmail(String email);
    void insertOrder(OrderDetailsDto orderDetailsDto, SessionCart sessionCart, String email);
}
