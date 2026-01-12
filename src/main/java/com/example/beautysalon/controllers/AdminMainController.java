package com.example.beautysalon.controllers;

import com.example.beautysalon.MainApp;
import com.example.beautysalon.models.Product;
import com.example.beautysalon.models.Service;
import com.example.beautysalon.services.CatalogService;
import com.example.beautysalon.services.OrderService;
import com.example.beautysalon.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class AdminMainController {
    @FXML private VBox mainContainer;
    private CatalogService catalogService = new CatalogService();
    private OrderService orderService = new OrderService();
    private UserService userService = new UserService();
    private ObservableList<Service> servicesList;
    private ObservableList<Product> productsList;

    @FXML
    public void initialize() {
        servicesList = FXCollections.observableArrayList(catalogService.getServices());
        productsList = FXCollections.observableArrayList(catalogService.getProducts());
        showMainMenu();
    }

    private void showMainMenu() {
        mainContainer.getChildren().clear();
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(30, 0, 40, 0));
        Label iconLabel = new Label("");
        iconLabel.setStyle("-fx-font-size: 48px;");
        Label titleLabel = new Label("Панель администратора");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: #8B008B;");
        Label subtitleLabel = new Label("Управление салоном красоты");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #666;");
        headerBox.getChildren().addAll(iconLabel, titleLabel, subtitleLabel);
        mainContainer.getChildren().add(headerBox);
        HBox toolbar = new HBox(10);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setPadding(new Insets(0, 0, 30, 0));
        Button logoutButton = new Button("Выйти");
        logoutButton.getStyleClass().add("btn-danger");
        logoutButton.setOnAction(e -> logout());
        toolbar.getChildren().add(logoutButton);
        mainContainer.getChildren().add(toolbar);
        FlowPane cardsPane = new FlowPane();
        cardsPane.setAlignment(Pos.CENTER);
        cardsPane.setHgap(30);
        cardsPane.setVgap(30);
        cardsPane.setPadding(new Insets(20));
        VBox servicesCard = createAdminCard("", "Управление услугами",
                "Добавление, редактирование и удаление услуг", "#8B008B");
        servicesCard.setOnMouseClicked(e -> manageServices());
        VBox productsCard = createAdminCard("", "Управление товарами",
                "Добавление, редактирование и удаление товаров", "#FF69B4");
        productsCard.setOnMouseClicked(e -> manageProducts());
        VBox ordersCard = createAdminCard("", "Просмотр заказов",
                "Просмотр и управление заказами клиентов", "#9400D3");
        ordersCard.setOnMouseClicked(e -> viewOrders());
        cardsPane.getChildren().addAll(servicesCard, productsCard, ordersCard);
        mainContainer.getChildren().add(cardsPane);
    }//dd

    private VBox createAdminCard(String emoji, String title, String description, String color) {
        VBox card = new VBox(15);
        card.getStyleClass().add("card");
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(280, 280);
        card.setStyle("-fx-border-color: " + color + ";");
        Label emojiLabel = new Label(emoji);
        emojiLabel.setStyle("-fx-font-size: 48px;");
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #666; -fx-text-alignment: center;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(240);
        Button actionButton = new Button("Перейти");
        actionButton.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
        actionButton.setOnAction(e -> {
            if (title.contains("услуг")) manageServices();
            else if (title.contains("товар")) manageProducts();
            else if (title.contains("заказ")) viewOrders();
        });
        card.getChildren().addAll(emojiLabel, titleLabel, descLabel, actionButton);
        return card;
    }

    private void manageServices() {
        mainContainer.getChildren().clear();
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(20));
        Button backButton = new Button("← Назад");
        backButton.getStyleClass().add("btn-secondary");
        backButton.setOnAction(e -> showMainMenu());
        Label title = new Label("Управление услугами");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #8B008B;");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setAlignment(Pos.CENTER);
        Button addButton = new Button("+ Добавить услугу");
        addButton.getStyleClass().add("btn-success");
        addButton.setOnAction(e -> showServiceForm(null));
        headerBox.getChildren().addAll(backButton, title, addButton);
        mainContainer.getChildren().add(headerBox);
        TableView<Service> table = new TableView<>();
        table.setItems(servicesList);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Service, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);
        TableColumn<Service, String> nameCol = new TableColumn<>("Название");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Service, Double> priceCol = new TableColumn<>("Цена");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(col -> new TableCell<Service, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f руб.", price));
                }
            }
        });
        TableColumn<Service, String> categoryCol = new TableColumn<>("Категория");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Service, Void> actionsCol = new TableColumn<>("Действия");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(param -> new TableCell<Service, Void>() {
            private final HBox buttons = new HBox(5);
            private final Button editButton = new Button("");
            private final Button deleteButton = new Button("");
            {
                buttons.setAlignment(Pos.CENTER);
                editButton.getStyleClass().add("btn-warning");
                editButton.setStyle("-fx-min-width: 30; -fx-min-height: 30; -fx-padding: 5;");
                editButton.setTooltip(new Tooltip("Редактировать"));
                deleteButton.getStyleClass().add("btn-danger");
                deleteButton.setStyle("-fx-min-width: 30; -fx-min-height: 30; -fx-padding: 5;");
                deleteButton.setTooltip(new Tooltip("Удалить"));
                editButton.setOnAction(event -> {
                    Service service = getTableView().getItems().get(getIndex());
                    showServiceForm(service);
                });
                deleteButton.setOnAction(event -> {
                    Service service = getTableView().getItems().get(getIndex());
                    deleteService(service);
                });
                buttons.getChildren().addAll(editButton, deleteButton);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });
        table.getColumns().addAll(idCol, nameCol, priceCol, categoryCol, actionsCol);
        VBox.setVgrow(table, Priority.ALWAYS);
        mainContainer.getChildren().add(table);
    }

    private void showServiceForm(Service service) {
        Dialog<Service> dialog = new Dialog<>();
        dialog.setTitle(service == null ? "Добавить услугу" : "Редактировать услугу");
        dialog.setHeaderText(service == null ? "Добавление новой услуги" : "Редактирование услуги");
        dialog.getDialogPane().setPrefSize(400, 300);
        dialog.setResizable(false);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        TextField nameField = new TextField();
        TextField priceField = new TextField();
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Маникюр", "Стрижка", "Проколы");
        TextField imageField = new TextField();
        if (service != null) {
            nameField.setText(service.getName());
            priceField.setText(String.valueOf((int)service.getPrice()));
            categoryCombo.setValue(service.getCategory());
            imageField.setText(service.getImagePath());
        } else {
            imageField.setText("/images/default.png");
        }
        grid.add(new Label("Название:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Цена (руб.):"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Категория:"), 0, 2);
        grid.add(categoryCombo, 1, 2);
        grid.add(new Label("Изображение:"), 0, 3);
        grid.add(imageField, 1, 3);
        dialog.getDialogPane().setContent(grid);
        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String name = nameField.getText().trim();
                    String priceText = priceField.getText().trim();
                    String category = categoryCombo.getValue();
                    String imagePath = imageField.getText().trim();
                    if (name.isEmpty() || priceText.isEmpty() || category == null) {
                        showAlert("Ошибка", "Все поля должны быть заполнены!");
                        return null;
                    }
                    double price = Double.parseDouble(priceText);
                    if (price <= 0) {
                        showAlert("Ошибка", "Цена должна быть положительной!");
                        return null;
                    }
                    if (service == null) {
                        Service newService = new Service(0, name, price, category, imagePath);
                        catalogService.addService(newService);
                        servicesList.setAll(catalogService.getServices());
                        return newService;
                    } else {
                        service.setName(name);
                        service.setPrice(price);
                        service.setCategory(category);
                        service.setImagePath(imagePath);
                        catalogService.updateService(service);
                        servicesList.setAll(catalogService.getServices());
                        return service;
                    }
                } catch (NumberFormatException e) {
                    showAlert("Ошибка", "Неверный формат цены!");
                    return null;
                }
            }
            return null;
        });
        Optional<Service> result = dialog.showAndWait();
        if (result.isPresent()) {
            showAlert("Успех", service == null ? "Услуга добавлена!" : "Услуга обновлена!");
        }
    }

    private void deleteService(Service service) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Удаление услуги");
        alert.setContentText("Вы уверены, что хотите удалить услугу \"" + service.getName() + "\"?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            catalogService.deleteService(service.getId());
            servicesList.setAll(catalogService.getServices());
            showAlert("Успех", "Услуга удалена!");
        }
    }

    private void manageProducts() {
        mainContainer.getChildren().clear();
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(20));
        Button backButton = new Button("← Назад");
        backButton.getStyleClass().add("btn-secondary");
        backButton.setOnAction(e -> showMainMenu());
        Label title = new Label("Управление товарами");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #FF69B4;");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setAlignment(Pos.CENTER);
        Button addButton = new Button("+ Добавить товар");
        addButton.getStyleClass().add("btn-success");
        addButton.setOnAction(e -> showProductForm(null));
        headerBox.getChildren().addAll(backButton, title, addButton);
        mainContainer.getChildren().add(headerBox);
        TableView<Product> table = new TableView<>();
        table.setItems(productsList);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(60);
        TableColumn<Product, String> nameCol = new TableColumn<>("Название");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product, Double> priceCol = new TableColumn<>("Цена");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(col -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f руб.", price));
                }
            }
        });
        TableColumn<Product, String> categoryCol = new TableColumn<>("Категория");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Product, Void> actionsCol = new TableColumn<>("Действия");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(param -> new TableCell<Product, Void>() {
            private final HBox buttons = new HBox(5);
            private final Button editButton = new Button("");
            private final Button deleteButton = new Button("");
            {
                buttons.setAlignment(Pos.CENTER);
                editButton.getStyleClass().add("btn-warning");
                editButton.setStyle("-fx-min-width: 30; -fx-min-height: 30; -fx-padding: 5;");
                editButton.setTooltip(new Tooltip("Редактировать"));
                deleteButton.getStyleClass().add("btn-danger");
                deleteButton.setStyle("-fx-min-width: 30; -fx-min-height: 30; -fx-padding: 5;");
                deleteButton.setTooltip(new Tooltip("Удалить"));
                editButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    showProductForm(product);
                });
                deleteButton.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    deleteProduct(product);
                });
                buttons.getChildren().addAll(editButton, deleteButton);
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        });
        table.getColumns().addAll(idCol, nameCol, priceCol, categoryCol, actionsCol);
        VBox.setVgrow(table, Priority.ALWAYS);
        mainContainer.getChildren().add(table);
    }

    private void showProductForm(Product product) {
        Dialog<Product> dialog = new Dialog<>();
        dialog.setTitle(product == null ? "Добавить товар" : "Редактировать товар");
        dialog.setHeaderText(product == null ? "Добавление нового товара" : "Редактирование товара");
        dialog.getDialogPane().setPrefSize(400, 300);
        dialog.setResizable(false);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        TextField nameField = new TextField();
        TextField priceField = new TextField();
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Уход за лицом", "Уход за телом");
        TextField imageField = new TextField();
        if (product != null) {
            nameField.setText(product.getName());
            priceField.setText(String.valueOf((int)product.getPrice()));
            categoryCombo.setValue(product.getCategory());
            imageField.setText(product.getImagePath());
        } else {
            imageField.setText("/images/default.png");
        }
        grid.add(new Label("Название:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Цена (руб.):"), 0, 1);
        grid.add(priceField, 1, 1);
        grid.add(new Label("Категория:"), 0, 2);
        grid.add(categoryCombo, 1, 2);
        grid.add(new Label("Изображение:"), 0, 3);
        grid.add(imageField, 1, 3);
        dialog.getDialogPane().setContent(grid);
        ButtonType saveButtonType = new ButtonType("Сохранить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String name = nameField.getText().trim();
                    String priceText = priceField.getText().trim();
                    String category = categoryCombo.getValue();
                    String imagePath = imageField.getText().trim();
                    if (name.isEmpty() || priceText.isEmpty() || category == null) {
                        showAlert("Ошибка", "Все поля должны быть заполнены!");
                        return null;
                    }
                    double price = Double.parseDouble(priceText);
                    if (price <= 0) {
                        showAlert("Ошибка", "Цена должна быть положительной!");
                        return null;
                    }
                    if (product == null) {
                        Product newProduct = new Product(0, name, price, category, imagePath);
                        catalogService.addProduct(newProduct);
                        productsList.setAll(catalogService.getProducts());
                        return newProduct;
                    } else {
                        product.setName(name);
                        product.setPrice(price);
                        product.setCategory(category);
                        product.setImagePath(imagePath);
                        catalogService.updateProduct(product);
                        productsList.setAll(catalogService.getProducts());
                        return product;
                    }
                } catch (NumberFormatException e) {
                    showAlert("Ошибка", "Неверный формат цены!");
                    return null;
                }
            }
            return null;
        });
        Optional<Product> result = dialog.showAndWait();
        if (result.isPresent()) {
            showAlert("Успех", product == null ? "Товар добавлен!" : "Товар обновлен!");
        }
    }

    private void deleteProduct(Product product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение удаления");
        alert.setHeaderText("Удаление товара");
        alert.setContentText("Вы уверены, что хотите удалить товар \"" + product.getName() + "\"?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            catalogService.deleteProduct(product.getId());
            productsList.setAll(catalogService.getProducts());
            showAlert("Успех", "Товар удален!");
        }
    }

    private void viewOrders() {
        mainContainer.getChildren().clear();
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(20));
        Button backButton = new Button("← Назад");
        backButton.getStyleClass().add("btn-secondary");
        backButton.setOnAction(e -> showMainMenu());
        Label title = new Label("Просмотр заказов");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #9400D3;");
        HBox.setHgrow(title, Priority.ALWAYS);
        title.setAlignment(Pos.CENTER);
        headerBox.getChildren().addAll(backButton, title);
        mainContainer.getChildren().add(headerBox);
        if (orderService.getOrders().isEmpty()) {
            Label noOrdersLabel = new Label("Заказов пока нет");
            noOrdersLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #666; -fx-padding: 40;");
            mainContainer.getChildren().add(noOrdersLabel);
        } else {
            TextArea ordersArea = new TextArea();
            ordersArea.setEditable(false);
            ordersArea.setStyle("-fx-font-size: 14px; -fx-font-family: 'Consolas', monospace;");
            StringBuilder ordersText = new StringBuilder();
            for (var order : orderService.getOrders()) {
                ordersText.append("════════════════════════════════════════\n");
                ordersText.append("Заказ #").append(order.getId()).append("\n");
                ordersText.append("Клиент: ").append(order.getCustomer()).append("\n");
                ordersText.append("Адрес: ").append(order.getDeliveryAddress()).append("\n");
                ordersText.append("Сумма: ").append(String.format("%.0f руб.", order.getTotalAmount())).append("\n");
                ordersText.append("Статус: ").append(order.getStatus()).append("\n");
                ordersText.append("Состав:\n");
                for (var item : order.getItems()) {
                    ordersText.append("  • ").append(item.getName())
                            .append(" x").append(item.getQuantity())
                            .append(" - ").append(String.format("%.0f руб.", item.getTotal()))
                            .append("\n");
                }
                ordersText.append("\n");
            }
            ordersArea.setText(ordersText.toString());
            VBox.setVgrow(ordersArea, Priority.ALWAYS);
            mainContainer.getChildren().add(ordersArea);
        }
    }

    @FXML
    private void logout() {
        userService.logout();
        MainApp.showLoginScreen();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}