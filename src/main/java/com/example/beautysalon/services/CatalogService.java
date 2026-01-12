package com.example.beautysalon.services;

import com.example.beautysalon.models.Product;
import com.example.beautysalon.models.Service;
import java.util.ArrayList;
import java.util.List;

public class CatalogService {
    private List<Service> services;
    private List<Product> products;
    private int serviceIdCounter = 1;
    private int productIdCounter = 1;

    public CatalogService() {
        initializeServices();
        initializeProducts();
    }

    private void initializeServices() {
        services = new ArrayList<>();
        services.add(new Service(serviceIdCounter++, "Маникюр гель-лак", 1500, "Маникюр", "/images/manicure.jpg"));
        services.add(new Service(serviceIdCounter++, "Стрижка женская", 1200, "Стрижка", "/images/haircut.jpg"));
        services.add(new Service(serviceIdCounter++, "Прокол ушей", 800, "Проколы", "/images/piercing.jpg"));
        services.add(new Service(serviceIdCounter++, "Педикюр", 1800, "Маникюр", "/images/pedicure.jpg"));
        services.add(new Service(serviceIdCounter++, "Окрашивание волос", 2500, "Стрижка", "/images/haircolor.jpg"));
        services.add(new Service(serviceIdCounter++, "Прокол носа", 1000, "Проколы", "/images/nosepiercing.jpg"));
        services.add(new Service(serviceIdCounter++, "Наращивание ресниц", 2000, "Уход за лицом", "/images/default.jpg"));
        services.add(new Service(serviceIdCounter++, "Макияж", 1500, "Уход за лицом", "/images/default.jpg"));
        services.add(new Service(serviceIdCounter++, "СПА-массаж", 3000, "Массаж", "/images/default.jpg"));
    }

    private void initializeProducts() {
        products = new ArrayList<>();
        products.add(new Product(productIdCounter++, "Крем для лица", 1200, "Уход за лицом", "/images/facecream.jpg"));
        products.add(new Product(productIdCounter++, "Лосьон для тела", 800, "Уход за телом", "/images/bodylotion.jpg"));
        products.add(new Product(productIdCounter++, "Сыворотка", 1800, "Уход за лицом", "/images/serum.jpg"));
        products.add(new Product(productIdCounter++, "Масло для тела", 950, "Уход за телом", "/images/bodyoil.jpg"));
        products.add(new Product(productIdCounter++, "Тоник для лица", 650, "Уход за лицом", "/images/toner.jpg"));
        products.add(new Product(productIdCounter++, "Скраб для тела", 750, "Уход за телом", "/images/scrub.jpg"));
        products.add(new Product(productIdCounter++, "Шампунь для волос", 850, "Уход за волосами", "/images/default.jpg"));
        products.add(new Product(productIdCounter++, "Бальзам для губ", 450, "Уход за лицом", "/images/default.jpg"));
        products.add(new Product(productIdCounter++, "Гель для душа", 550, "Уход за телом", "/images/default.jpg"));
        products.add(new Product(productIdCounter++, "Гель-лак", 600, "Маникюр", "/images/default.jpg"));
    }

    public List<Service> getServices() {
        return new ArrayList<>(services);
    }

    public List<Service> getServicesByCategory(String category) {
        List<Service> filtered = new ArrayList<>();
        for (Service service : services) {
            if (service.getCategory().equals(category)) {
                filtered.add(service);
            }
        }
        return filtered;
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public List<Product> getProductsByCategory(String category) {
        List<Product> filtered = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory().equals(category)) {
                filtered.add(product);
            }
        }
        return filtered;
    }

    public void addService(Service service) {
        service.setId(serviceIdCounter++);
        services.add(service);
    }

    public void updateService(Service updatedService) {
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getId() == updatedService.getId()) {
                services.set(i, updatedService);
                break;
            }
        }
    }

    public void deleteService(int id) {
        services.removeIf(s -> s.getId() == id);
    }

    public void addProduct(Product product) {
        product.setId(productIdCounter++);
        products.add(product);
    }

    public void updateProduct(Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == updatedProduct.getId()) {
                products.set(i, updatedProduct);
                break;
            }
        }
    }

    public void deleteProduct(int id) {
        products.removeIf(p -> p.getId() == id);
    }
}