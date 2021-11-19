package com.company.repository;

import com.company.model.Order;

import java.util.HashMap;
import java.util.List;

public interface OrderRepository {
    List<Order> getAllOrders();
    List<Order> getOrdersByUserEmail(String email);
    void insertOrder(Order order, HashMap<Integer, Integer> quantitiesForUpdate);
}
