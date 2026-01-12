package com.example.beautysalon.controllers;

import com.example.beautysalon.MainApp;
import com.example.beautysalon.models.Product;
import com.example.beautysalon.models.Service;
import com.example.beautysalon.services.CartService;
import com.example.beautysalon.services.CatalogService;
import com.example.beautysalon.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.InputStream;

public class UserMainController {
    @FXML private VBox mainContainer;
    @FXML private Button cartButton;
    @FXML private Label cartCountLabel;
    private CatalogService catalogService = new CatalogService();
    private CartService cartService = new CartService();
    private UserService userService = new UserService();

    @FXML
    public void initialize() {
        updateCartCount();
        showMainMenu();
    }

    private void showMainMenu() {
        mainContainer.getChildren().clear();
        VBox welcomeBox = new VBox(10);
        welcomeBox.setAlignment(Pos.CENTER);
        welcomeBox.setPadding(new Insets(30, 0, 40, 0));
        Label welcomeIcon = new Label("");
        welcomeIcon.setStyle("-fx-font-size: 48px;");
        Label welcomeText = new Label("Добро пожаловать в Studio Mariana");
        welcomeText.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #8B008B;");
        Label subtitle = new Label("Выберите категорию");
        subtitle.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
        welcomeBox.getChildren().addAll(welcomeIcon, welcomeText, subtitle);
        mainContainer.getChildren().add(welcomeBox);
        FlowPane categoriesPane = new FlowPane();
        categoriesPane.setAlignment(Pos.CENTER);
        categoriesPane.setHgap(30);
        categoriesPane.setVgap(30);
        categoriesPane.setPadding(new Insets(20));
        VBox servicesCard = createCategoryCard("Услуги", "",
                "Маникюр, стрижки, проколы и другие услуги", "#8B008B");
        servicesCard.setOnMouseClicked(e -> showServicesCatalog());
        VBox productsCard = createCategoryCard("Товары", "",
                "Косметика для ухода за лицом и телом", "#FF69B4");
        productsCard.setOnMouseClicked(e -> showProductsCatalog());
        categoriesPane.getChildren().addAll(servicesCard, productsCard);
        mainContainer.getChildren().add(categoriesPane);
    }

    private VBox createCategoryCard(String title, String emoji, String description, String color) {
        VBox card = new VBox(15);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(300, 250);
        card.setStyle("-fx-border-color: " + color + ";");
        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 48px;");
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-text-alignment: center;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(250);
        Button selectButton = new Button("Выбрать");
        selectButton.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        selectButton.setOnAction(e -> {
            if (title.equals("Услуги")) showServicesCatalog();
            else showProductsCatalog();
        });
        card.getChildren().addAll(emojiLabel, titleLabel, descLabel, selectButton);
        return card;
    }

    private void showServicesCatalog() {
        mainContainer.getChildren().clear();
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(20));
        Button backButton = new Button("← Назад");
        backButton.getStyleClass().add("btn-secondary");
        backButton.setOnAction(e -> showMainMenu());
        Label title = new Label("Наши услуги");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #8B008B;");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setAlignment(Pos.CENTER);
        headerBox.getChildren().addAll(backButton, title);
        mainContainer.getChildren().add(headerBox);
        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER);
        filterBox.setPadding(new Insets(0, 0, 20, 0));
        ToggleGroup categoryGroup = new ToggleGroup();
        ToggleButton allButton = new ToggleButton("Все");
        allButton.setToggleGroup(categoryGroup);
        allButton.setSelected(true);
        allButton.getStyleClass().add("btn-primary");
        ToggleButton manicureButton = new ToggleButton("Маникюр");
        manicureButton.setToggleGroup(categoryGroup);
        manicureButton.getStyleClass().add("btn-primary");
        ToggleButton haircutButton = new ToggleButton("Стрижка");
        haircutButton.setToggleGroup(categoryGroup);
        haircutButton.getStyleClass().add("btn-primary");
        ToggleButton piercingButton = new ToggleButton("Проколы");
        piercingButton.setToggleGroup(categoryGroup);
        piercingButton.getStyleClass().add("btn-primary");
        categoryGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == allButton) displayServices(null);
            else if (newVal == manicureButton) displayServices("Маникюр");
            else if (newVal == haircutButton) displayServices("Стрижка");
            else if (newVal == piercingButton) displayServices("Проколы");
        });
        filterBox.getChildren().addAll(allButton, manicureButton, haircutButton, piercingButton);
        mainContainer.getChildren().add(filterBox);
        displayServices(null);
    }

    private void displayServices(String category) {
        if (mainContainer.getChildren().size() > 2) {
            mainContainer.getChildren().remove(2, mainContainer.getChildren().size());
        }
        FlowPane servicesPane = new FlowPane();
        servicesPane.setAlignment(Pos.TOP_CENTER);
        servicesPane.setHgap(20);
        servicesPane.setVgap(20);
        servicesPane.setPadding(new Insets(20));
        var services = category == null ?
                catalogService.getServices() :
                catalogService.getServicesByCategory(category);
        for (Service service : services) {
            VBox serviceCard = createServiceCard(service);
            servicesPane.getChildren().add(serviceCard);
        }
        mainContainer.getChildren().add(servicesPane);
    }

    private ImageView createImageView(String imagePath) {
        ImageView imageView = new ImageView();
        try {
            InputStream inputStream = getClass().getResourceAsStream(imagePath);
            if (inputStream != null) {
                Image image = new Image(inputStream);
                imageView.setImage(image);
            } else {
                System.err.println("Image not found: " + imagePath);
                imageView.setStyle("-fx-background-color: #8B008B; -fx-min-width: 160; -fx-min-height: 160; -fx-background-radius: 10;");
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + imagePath + " - " + e.getMessage());
            imageView.setStyle("-fx-background-color: #F0F0F0; -fx-min-width: 160; -fx-min-height: 160; -fx-background-radius: 10;");
        }
        imageView.setFitWidth(160);
        imageView.setFitHeight(160);
        imageView.setPreserveRatio(true);
        imageView.setStyle(imageView.getStyle() + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 0);");
        return imageView;
    }

    private VBox createServiceCard(Service service) {
        VBox card = new VBox(15);
        card.getStyleClass().addAll("card", "card-service");
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(250, 380);
        card.setPadding(new Insets(20));
        ImageView imageView = createImageView(service.getImagePath());
        card.getChildren().add(imageView);
        Label nameLabel = new Label(service.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-alignment: center; -fx-text-fill: #333;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(200);
        Label priceLabel = new Label(String.format("%.0f руб.", service.getPrice()));
        priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #8B008B;");
        Button selectButton = new Button("В корзину");
        selectButton.getStyleClass().add("btn-primary");
        selectButton.setOnAction(e -> {
            cartService.addToCart(service, "Service", service.getName(), service.getPrice(), service.getCategory());
            updateCartCount();
            selectButton.setText("✓ Добавлено!");
            selectButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> {
                        selectButton.setText("В корзину");
                        selectButton.setStyle("-fx-background-color: #8B008B; -fx-text-fill: white;");
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        });
        card.getChildren().addAll(nameLabel, priceLabel, selectButton);
        return card;
    }

    private String getServiceIcon(String category) {
        switch (category) {
            case "Маникюр": return "";
            case "Стрижка": return "";
            case "Проколы": return "";
            default: return "🛠";
        }
    }

    private void showProductsCatalog() {
        mainContainer.getChildren().clear();
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(20));
        Button backButton = new Button("← Назад");
        backButton.getStyleClass().add("btn-secondary");
        backButton.setOnAction(e -> showMainMenu());
        Label title = new Label("Наши товары");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #FF69B4;");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setAlignment(Pos.CENTER);
        headerBox.getChildren().addAll(backButton, title);
        mainContainer.getChildren().add(headerBox);
        HBox filterBox = new HBox(10);
        filterBox.setAlignment(Pos.CENTER);
        filterBox.setPadding(new Insets(0, 0, 20, 0));
        ToggleGroup categoryGroup = new ToggleGroup();
        ToggleButton allButton = new ToggleButton("Все");
        allButton.setToggleGroup(categoryGroup);
        allButton.setSelected(true);
        allButton.getStyleClass().add("btn-secondary");
        ToggleButton faceButton = new ToggleButton("Уход за лицом");
        faceButton.setToggleGroup(categoryGroup);
        faceButton.getStyleClass().add("btn-secondary");
        ToggleButton bodyButton = new ToggleButton("Уход за телом");
        bodyButton.setToggleGroup(categoryGroup);
        bodyButton.getStyleClass().add("btn-secondary");
        categoryGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == allButton) displayProducts(null);
            else if (newVal == faceButton) displayProducts("Уход за лицом");
            else if (newVal == bodyButton) displayProducts("Уход за телом");
        });
        filterBox.getChildren().addAll(allButton, faceButton, bodyButton);
        mainContainer.getChildren().add(filterBox);
        displayProducts(null);
    }

    private void displayProducts(String category) {
        if (mainContainer.getChildren().size() > 2) {
            mainContainer.getChildren().remove(2, mainContainer.getChildren().size());
        }
        FlowPane productsPane = new FlowPane();
        productsPane.setAlignment(Pos.TOP_CENTER);
        productsPane.setHgap(20);
        productsPane.setVgap(20);
        productsPane.setPadding(new Insets(20));
        var products = category == null ?
                catalogService.getProducts() :
                catalogService.getProductsByCategory(category);
        for (Product product : products) {
            VBox productCard = createProductCard(product);
            productsPane.getChildren().add(productCard);
        }
        mainContainer.getChildren().add(productsPane);
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(15);
        card.getStyleClass().addAll("card", "card-product");
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(250, 380);
        card.setPadding(new Insets(20));
        ImageView imageView = createImageView(product.getImagePath());
        card.getChildren().add(imageView);
        Label nameLabel = new Label(product.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-alignment: center; -fx-text-fill: #333;");
        nameLabel.setWrapText(true);
        nameLabel.setMaxWidth(200);
        Label priceLabel = new Label(String.format("%.0f руб.", product.getPrice()));
        priceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #FF69B4;");
        Button selectButton = new Button("В корзину");
        selectButton.getStyleClass().add("btn-secondary");
        selectButton.setOnAction(e -> {
            cartService.addToCart(product, "Product", product.getName(), product.getPrice(), product.getCategory());
            updateCartCount();
            selectButton.setText("✓ Добавлено!");
            selectButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> {
                        selectButton.setText("В корзину");
                        selectButton.setStyle("-fx-background-color: #FF69B4; -fx-text-fill: white;");
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        });
        card.getChildren().addAll(nameLabel, priceLabel, selectButton);
        return card;
    }

    private String getProductIcon(String category) {
        switch (category) {
            case "Уход за лицом": return "";
            case "Уход за телом": return "";
            default: return "";
        }
    }

    @FXML
    private void openCart() {
        CartController.showCart(cartService, this::updateCartCount);
    }

    @FXML
    private void logout() {
        userService.logout();
        MainApp.showLoginScreen();
    }

    private void updateCartCount() {
        int count = cartService.getItemCount();
        cartCountLabel.setText(String.valueOf(count));
    }
}