package com.example.beautysalon.services;

import com.example.beautysalon.models.Order;
import com.example.beautysalon.models.CartItem;
import java.util.ArrayList;
import java.util.List;

public class OrderService {
    private List<Order> orders;
    private int orderIdCounter = 1;

    public OrderService() {
        orders = new ArrayList<>();
    }

    public Order createOrder(String customer, List<CartItem> items, String deliveryAddress) {
        double totalAmount = 0;
        for (CartItem item : items) {
            totalAmount += item.getTotal();
        }
        Order order = new Order(orderIdCounter++, customer, new ArrayList<>(items), totalAmount, deliveryAddress);
        orders.add(order);
        return order;
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }
}