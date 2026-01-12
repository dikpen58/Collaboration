package com.example.beautysalon.controllers;

import com.example.beautysalon.models.CartItem;
import com.example.beautysalon.services.CartService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;

public class CartController {
    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> nameCol;
    @FXML private TableColumn<CartItem, String> typeCol;
    @FXML private TableColumn<CartItem, Double> priceCol;
    @FXML private TableColumn<CartItem, Integer> quantityCol;
    @FXML private TableColumn<CartItem, Double> totalCol;
    @FXML private TableColumn<CartItem, Void> actionsCol;
    @FXML private Label totalLabel;
    @FXML private Button backButton;
    @FXML private Button checkoutButton;

    private CartService cartService;
    private Runnable updateCallback;
    private Stage stage;
    private ObservableList<CartItem> cartItems;

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
        cartItems = FXCollections.observableArrayList(cartService.getCartItems());
        cartTable.setItems(cartItems);
        updateTotal();
    }

    public void setUpdateCallback(Runnable callback) {
        this.updateCallback = callback;
    }

    @FXML
    public void initialize() {
        nameCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));

        typeCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));

        priceCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        priceCol.setCellFactory(col -> new TableCell<CartItem, Double>() {
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

        quantityCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        totalCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getTotal()).asObject());
        totalCol.setCellFactory(col -> new TableCell<CartItem, Double>() {
            @Override
            protected void updateItem(Double total, boolean empty) {
                super.updateItem(total, empty);
                if (empty || total == null) {
                    setText(null);
                } else {
                    setText(String.format("%.0f руб.", total));
                    setStyle("-fx-font-weight: bold;");
                }
            }
        });

        actionsCol.setCellFactory(param -> new TableCell<CartItem, Void>() {
            private final HBox buttons = new HBox(5);
            private final Button plusButton = new Button("+");
            private final Button minusButton = new Button("-");
            private final Button deleteButton = new Button("🗑️");

            {
                buttons.setAlignment(javafx.geometry.Pos.CENTER);

                plusButton.getStyleClass().add("btn-success");
                plusButton.setStyle("-fx-min-width: 30; -fx-min-height: 30; -fx-padding: 5;");

                minusButton.getStyleClass().add("btn-warning");
                minusButton.setStyle("-fx-min-width: 30; -fx-min-height: 30; -fx-padding: 5;");

                deleteButton.getStyleClass().add("btn-danger");
                deleteButton.setStyle("-fx-min-width: 30; -fx-min-height: 30; -fx-padding: 5;");

                plusButton.setOnAction(event -> {
                    CartItem item = getTableView().getItems().get(getIndex());
                    cartService.updateQuantity(item, item.getQuantity() + 1);
                    refreshTable();
                });

                minusButton.setOnAction(event -> {
                    CartItem item = getTableView().getItems().get(getIndex());
                    if (item.getQuantity() > 1) {
                        cartService.updateQuantity(item, item.getQuantity() - 1);
                    } else {
                        cartService.removeFromCart(item);
                    }
                    refreshTable();
                });

                deleteButton.setOnAction(event -> {
                    CartItem item = getTableView().getItems().get(getIndex());
                    cartService.removeFromCart(item);
                    refreshTable();
                });

                buttons.getChildren().addAll(plusButton, minusButton, deleteButton);
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
    }

    private void refreshTable() {
        cartItems.setAll(cartService.getCartItems());
        updateTotal();
        if (updateCallback != null) {
            updateCallback.run();
        }
    }

    private void updateTotal() {
        double total = cartService.getTotalAmount();
        totalLabel.setText(String.format("Итого к оплате: %.0f руб.", total));
        totalLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #8B008B;");
    }

    @FXML
    private void handleBack() {
        stage.close();
    }

    @FXML
    private void handleCheckout() {
        if (cartService == null || cartService.getCartItems().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Корзина пуста",
                    "Добавьте товары в корзину перед оформлением заказа.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/checkout.fxml"));
            Parent root = loader.load();

            CheckoutController controller = loader.getController();
            controller.setCartService(cartService);
            controller.setUpdateCallback(() -> {
                refreshTable();
                if (updateCallback != null) {
                    updateCallback.run();
                }
            });

            Stage checkoutStage = new Stage();
            controller.setStage(checkoutStage);
            checkoutStage.initModality(Modality.APPLICATION_MODAL);
            checkoutStage.setTitle("Оформление заказа - Studio Mariana");
            checkoutStage.setScene(new Scene(root, 600, 500));
            checkoutStage.setMinWidth(600);
            checkoutStage.setMinHeight(500);
            checkoutStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Ошибка",
                    "Не удалось открыть форму оформления заказа: " + e.getMessage());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public static void showCart(CartService cartService, Runnable updateCallback) {
        try {
            FXMLLoader loader = new FXMLLoader(CartController.class.getResource("/views/cart.fxml"));
            Parent root = loader.load();

            CartController controller = loader.getController();
            controller.setCartService(cartService);
            controller.setUpdateCallback(updateCallback);

            Stage cartStage = new Stage();
            controller.setStage(cartStage);
            cartStage.initModality(Modality.APPLICATION_MODAL);
            cartStage.setTitle("Корзина покупок - Studio Mariana");
            cartStage.setScene(new Scene(root, 1000, 700));
            cartStage.setMinWidth(1000);
            cartStage.setMinHeight(700);
            cartStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}