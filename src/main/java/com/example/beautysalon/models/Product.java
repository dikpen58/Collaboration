package com.example.beautysalon.models;

import javafx.beans.property.*;

public class Product {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final StringProperty category = new SimpleStringProperty();
    private final StringProperty imagePath = new SimpleStringProperty();

    public Product() {}

    public Product(int id, String name, double price, String category, String imagePath) {
        setId(id);
        setName(name);
        setPrice(price);
        setCategory(category);
        setImagePath(imagePath);
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

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public String getImagePath() {
        return imagePath.get();
    }

    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }

    public StringProperty imagePathProperty() {
        return imagePath;
    }
}