package com.example.beautysalon.controllers;

import com.example.beautysalon.services.CartService;
import com.example.beautysalon.services.OrderService;
import com.example.beautysalon.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CheckoutController {
    @FXML private TextArea addressField;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;
    @FXML private Label itemsLabel;
    @FXML private Label totalLabel;
    private CartService cartService;
    private Runnable updateCallback;
    private OrderService orderService = new OrderService();
    private UserService userService = new UserService();
    private Stage stage;

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
        updateOrderSummary();
    }

    public void setUpdateCallback(Runnable callback) {
        this.updateCallback = callback;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
    }

    private void updateOrderSummary() {
        if (cartService == null) return;
        StringBuilder itemsText = new StringBuilder("Ваш заказ:\n");
        for (var item : cartService.getCartItems()) {
            itemsText.append(String.format("• %s x%d - %.0f руб.\n",
                    item.getName(), item.getQuantity(), item.getTotal()));
        }
        itemsLabel.setText(itemsText.toString());
        double total = cartService.getTotalAmount();
        totalLabel.setText(String.format("Итого: %.0f руб.", total));
        totalLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #8B008B;");
    }

    @FXML
    private void handleSubmit() {
        String address = addressField.getText().trim();
        if (address.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Ошибка",
                    "Пожалуйста, введите адрес доставки.");
            return;
        }
        if (address.length() < 10) {
            showAlert(Alert.AlertType.WARNING, "Внимание",
                    "Адрес доставки должен содержать не менее 10 символов.");
            return;
        }
        String customer = userService.getCurrentUser() != null ?
                userService.getCurrentUser().getUsername() : "Гость";
        var order = orderService.createOrder(customer, cartService.getCartItems(), address);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Заказ оформлен!");
        alert.setHeaderText("Ваш заказ успешно оформлен");
        alert.setContentText(
                "Заказ #" + order.getId() + " оплачен и отправлен по адресу:\n" +
                        address + "\n\n" +
                        "Ожидайте доставки в течение 1-2 дней!\n" +
                        "Спасибо за покупку в Studio Mariana! "
        );
        alert.showAndWait();
        cartService.clearCart();
        if (stage != null) {
            stage.close();
        }
        if (updateCallback != null) {
            updateCallback.run();
        }
    }

    @FXML
    private void handleCancel() {
        if (stage != null) {
            stage.close();
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