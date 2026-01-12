package com.example.beautysalon.models;

import javafx.beans.property.*;
import java.util.List;

public class Order {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty customer = new SimpleStringProperty();
    private List<CartItem> items;
    private final DoubleProperty totalAmount = new SimpleDoubleProperty();
    private final StringProperty deliveryAddress = new SimpleStringProperty();
    private final StringProperty status = new SimpleStringProperty();

    public Order(int id, String customer, List<CartItem> items, double totalAmount, String deliveryAddress) {
        setId(id);
        setCustomer(customer);
        this.items = items;
        setTotalAmount(totalAmount);
        setDeliveryAddress(deliveryAddress);
        setStatus("В обработке");
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getCustomer() {
        return customer.get();
    }

    public void setCustomer(String customer) {
        this.customer.set(customer);
    }

    public StringProperty customerProperty() {
        return customer;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount.get();
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount.set(totalAmount);
    }

    public DoubleProperty totalAmountProperty() {
        return totalAmount;
    }

    public String getDeliveryAddress() {
        return deliveryAddress.get();
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress.set(deliveryAddress);
    }

    public StringProperty deliveryAddressProperty() {
        return deliveryAddress;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public StringProperty statusProperty() {
        return status;
    }
}