package com.example.beautysalon.models;

public class CartItem {
    private Object item;
    private String type;
    private String name;
    private double price;
    private int quantity;
    private String category;

    public CartItem(Object item, String type, String name, double price, int quantity, String category) {
        this.item = item;
        this.type = type;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return price * quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}